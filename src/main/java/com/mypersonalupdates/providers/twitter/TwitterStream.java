package com.mypersonalupdates.providers.twitter;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.users.User;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Esta clase representa una conexión a la API de Streaming
 * de Twitter.com. Está asociada a un usuario del sistema
 * específico y se encarga de despachar las actualizaciones
 * en tiempo real pedidas por sus suscriptores.
 * */
public final class TwitterStream implements StatusListener {
    private static final List<Long> DB_FOLLOWINGS_ATTR_IDS = Config.get().getLongList("providers.twitter.DBFollowingsAttrIDs");
    private static final List<Long> DB_TRACK_TERMS_ATTR_IDS = Config.get().getLongList("providers.twitter.DBTrackTermsAttrIDs");
    private static final List<String> TWEET_SEARCH_LANGS = Config.get().getStringList("providers.twitter.TweetSearchLanguages");
    private static final boolean ENABLE_STALL_WARNINGS = Config.get().getBoolean("providers.twitter.EnableStallWarnings");
    private static final int MAX_PROCESSING_THREADS = Config.get().getInt("providers.twitter.maxMsgProcessingThreadsPerUser");
    private static final int MSG_QUEUE_SIZE = Config.get().getInt("providers.twitter.msgQueueSize");

    private final Hosts hosts = new HttpHosts(Constants.STREAM_HOST);
    private final Authentication authentication;
    private final long userID;
    private Twitter4jStatusClient t4jClient = null;

    private final Map<Filter, Map<Long, UpdatesConsumer>> consumers = new Hashtable<>();
    private final Map<Long, Filter> filtersById = new Hashtable<>();
    private Long nextSubscriberID = 0L;

    TwitterStream(TwitterProvider provider, User user) throws DBException, UserNotLoggedInToProviderException {
        this.userID = user.getID();

        String accessToken = user.getAttribute(provider, "accessToken");
        String accessTokenSecret = user.getAttribute(provider, "accessTokenSecret");

        if(accessToken == null || accessTokenSecret == null)
            throw new UserNotLoggedInToProviderException("Usuario no logueado en Twitter.");

        this.authentication = new OAuth1(
                Config.get().getString("providers.twitter.consumerKey"),
                Config.get().getString("providers.twitter.consumerSecret"),

                accessToken,
                accessTokenSecret
        );
    }

    public Long subscribe(Filter filter, UpdatesConsumer consumer) {
        boolean isNewFilter = false;
        Long id;

        synchronized (this.consumers) {
            Map<Long, UpdatesConsumer> consumers = this.consumers.get(filter);

            if (consumers == null) {
                consumers = new Hashtable<>();
                this.consumers.put(filter, consumers);
                isNewFilter = true;
            }

            id = this.nextSubscriberID++;
            consumers.put(id, consumer);
            this.filtersById.put(id, filter);
        }

        if (isNewFilter)
            this.reconnect();

        return id;
    }

    private Collection<String> getAttributeValuesFromFilters(UpdatesProviderAttribute attr) {
        Collection<String> ret = new LinkedList<>();

        for (Filter filter : this.filtersById.values()) {
            Collection<String> values = filter.getValues(attr);
            if (values != null)
                ret.addAll(values);
        }

        return ret;
    }

    private StatusesFilterEndpoint createEndpointFromFilters() {
        List<Long> followings = new LinkedList<>();
        List<String> terms = new LinkedList<>();

        for (UpdatesProviderAttribute attr : TwitterProvider.getInstance().getAttributes()) {
            Collection<String> values = this.getAttributeValuesFromFilters(attr);

            System.out.println("DB_FOLLOWINGS_ATTR_IDS = " + DB_FOLLOWINGS_ATTR_IDS);
            System.out.println("attr.getAttrID() = " + attr.getAttrID());
            System.out.println("values = " + values);

            if (values.size() == 0)
                continue;

            if (TwitterStream.DB_FOLLOWINGS_ATTR_IDS.contains(attr.getAttrID())) {
                followings.addAll(
                        Collections2.transform(values, Long::valueOf)
                );
            } else if (TwitterStream.DB_TRACK_TERMS_ATTR_IDS.contains(attr.getAttrID())) {
                terms.addAll(values);
            }
        }

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        endpoint.stallWarnings(TwitterStream.ENABLE_STALL_WARNINGS);
        endpoint.languages(TwitterStream.TWEET_SEARCH_LANGS);

        if (followings.size() > 0) {
            endpoint.followings(followings);
            System.out.println("followings = " + followings);
        }

        if (terms.size() > 0) {
            endpoint.trackTerms(terms);
        }

        return followings.size() == 0 && terms.size() == 0 ? null : endpoint;
    }

    private void reconnect() {
        synchronized (this.consumers) {

            BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(TwitterStream.MSG_QUEUE_SIZE);

            StatusesFilterEndpoint endpoint = this.createEndpointFromFilters();

            assert endpoint != null;

            ClientBuilder builder = new ClientBuilder()
                    .name("Twitter client for user " + this.userID)
                    .hosts(this.hosts)
                    .authentication(this.authentication)
                    .endpoint(endpoint)
                    .processor(new StringDelimitedProcessor(msgQueue));

            Client hosebirdClient = builder.build();

            if (this.t4jClient != null)
                this.t4jClient.stop();

            this.t4jClient = new Twitter4jStatusClient(
                    hosebirdClient, // twitter client to connect to
                    msgQueue, // queue where messages are stored
                    Lists.newArrayList(this), // listeners
                    Executors.newCachedThreadPool() // thread pool for processing messages
            );

            // attempt to connect
            this.t4jClient.connect();

            for (int i = TwitterStream.MAX_PROCESSING_THREADS; i > 0; --i)
                this.t4jClient.process(); // creates a thread for processing of raw messages
        }
    }

    public boolean unsubscribe(Long subscriberID) {
        synchronized (this.consumers) {
            Filter filter = this.filtersById.remove(subscriberID);
            if (filter == null)
                return false;

            Map<Long, UpdatesConsumer> consumers = this.consumers.get(filter);
            if (consumers == null)
                return false;

            UpdatesConsumer consumer = consumers.remove(subscriberID);

            if (consumers.size() == 0)
                this.consumers.remove(filter);

            return consumer != null;
        }
    }

    void stop() {
        synchronized (this.consumers) {
            if (this.t4jClient != null)
                this.t4jClient.stop();

            this.consumers.clear();
            this.filtersById.clear();
        }
    }

    @Override
    public void onStatus(Status status) {
        TwitterUpdate update = new TwitterUpdate(status);

        synchronized (this.consumers) {
            this.consumers.forEach((filter, consumers) -> {
                if (filter.test(update)) {
                    for (UpdatesConsumer consumer : consumers.values())
                        consumer.handleUpdate(update);
                }
            });
        }

        if(status.isRetweet())
            this.onStatus(status.getRetweetedStatus());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        // TODO: manejar apropiadamente
    }

    @Override
    public void onTrackLimitationNotice(int i) {
    }

    @Override
    public void onScrubGeo(long l, long l1) {
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
    }

    @Override
    public void onException(Exception e) {
        System.err.println("Twitter exception: " + e.getMessage());
        e.printStackTrace();
        // TODO: manejar apropiadamente
    }
}
