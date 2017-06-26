package com.mypersonalupdates.providers.twitter;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;
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

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStream implements StatusListener {
    // TODO: implement hashCode in Filter and UpdatesConsumer
    // TODO: Filter as object with dynamic attributes

    private final Hosts hosts = new HttpHosts(Constants.STREAM_HOST);
    private final Authentication authentication;
    private final int userID;
    private Twitter4jStatusClient t4jClient = null;

    private final Hashtable<Filter, BiMap<UpdatesConsumer, Integer>> consumers = new Hashtable<>();
    private final Hashtable<Integer, Filter> filtersById = new Hashtable<>();
    private Integer nextSubscriberId = 0;

    public TwitterStream(User user) {
        this.userID = user.getId();

        // These secrets should be read from a config file
        this.authentication = new OAuth1(
                // TODO: read from file with global config object
                "KBloN9bYM0xSgHdYlCvIgi4Ov",
                "PGQqpQMneAvh2LBDiZBArZ51GOTsPAaNgoxyAxMEER50rBseXd",

                // TODO: obtain from user-specific provider settings
                // TODO: dynamic attributes in user (specific to providers)
                "98758588-pRnp7Qndq4faaMaNXRFDctD3acg56YL0Dugpu4VEf",
                "68iikmgVPgyzhl3tjP2kSKzuW2g0R1CVKXuIHPUYDpvqc"
        );
    }

    public Integer subscribe(Filter filter, UpdatesConsumer consumer) {
        boolean isNewFilter = false;
        Integer id;
        synchronized (this.consumers) {
            BiMap<UpdatesConsumer, Integer> consumers = this.consumers.get(filter);
            if (consumers != null) {
                id = consumers.get(consumer);
                if (id == null)
                    id = consumers.put(consumer, this.nextSubscriberId++);
                // TODO: handle this case properly
                // else, in this case the consumer is already listening for updates with that filter
                // we should keep track of how many times a given id has been used in order to delete
                // the consumer only when it's not being used
            } else {
                consumers = HashBiMap.create();
                id = this.nextSubscriberId++;
                consumers.put(consumer, id);
                this.consumers.put(filter, consumers);
                this.filtersById.put(id, filter);
                isNewFilter = true;
            }
        }

        if (isNewFilter)
            this.reconnect();

        return id;
    }

    private void reconnect() {
        synchronized (this.consumers) {
            // TODO: generate twitter filters from Filter
            // List<Long> followings = Lists.newArrayList(1234L, 566788L);
            List<String> terms = Lists.newArrayList("argentina");
            List<String> langs = Lists.newArrayList("es", "en");

            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            //  endpoint.followings(followings);
            endpoint.trackTerms(terms);
            endpoint.languages(langs);

            BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);

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

            // TODO: config for amount of threads
            this.t4jClient.process(); // creates a thread for processing of raw messages
        }
    }

    public boolean unsubscribe(int subscriberID) {
        synchronized (this.consumers) {
            // TODO: check if this id is being used more than once

            Filter filter = this.filtersById.remove(subscriberID);
            if(filter == null) return false;

            BiMap<UpdatesConsumer, Integer> consumers = this.consumers.get(filter);
            if(consumers == null) return false;

            UpdatesConsumer consumer = consumers.inverse().remove(subscriberID);

            if(consumers.size() == 0)
                this.consumers.remove(filter);

            return consumer != null;
        }
    }

    @Override
    public void onStatus(Status status) {
        TwitterUpdate update = new TwitterUpdate(status);

        synchronized (this.consumers) {
            this.consumers.forEach((filter, consumers) -> {
                // TODO: update when filter test is implemented
                //if(filter.test(update)) {
                    consumers.keySet().forEach(consumer -> consumer.handleUpdate(update));
                //}
            });
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        // TODO: handle properly
        System.out.println("status deleted: "+statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        // TODO: handle properly
        System.err.println("Twitter track limitation notice: "+i);
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        // TODO: handle properly
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        // TODO: handle properly
        System.err.println("Twitter stall warning: "+stallWarning.getMessage());
    }

    @Override
    public void onException(Exception e) {
        System.err.println("Twitter exception: "+e.getMessage());
        e.printStackTrace();
        // TODO: handle properly
    }
}
