package com.mypersonalupdates.providers.twitter;

import com.google.common.collect.Lists;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStream {
    private final List<TwitterListener> listeners = Collections.synchronizedList(new LinkedList<>());

    // TODO: implement hashCode in Filter and UpdatesConsumer
    // TODO: Filter as object with dynamic attributes
//    private Hashtable<Filter, Hashtable<UpdatesConsumer, Integer>> consumers = new Hashtable<>();
//    private Hashtable<UpdatesConsumer, Integer> usageCount = new Hashtable<>();

    private final Hosts hosts = new HttpHosts(Constants.STREAM_HOST);
    private final Authentication authentication;
    private final int userID;
    private Twitter4jStatusClient t4jClient;

    //private Integer nextSubscriberID = 0;

    public TwitterStream(User user) {
        // TODO: use only one TwitterListener to create the Update and be able to add new listeners without reconnecting

        this.userID = user.getId();

        // These secrets should be read from a config file
        this.authentication = new OAuth1(
                // TODO: read from file
                "KBloN9bYM0xSgHdYlCvIgi4Ov",
                "PGQqpQMneAvh2LBDiZBArZ51GOTsPAaNgoxyAxMEER50rBseXd",

                // TODO: obtain from user-specific provider settings
                // TODO: dynamic attributes in user (specific to providers)
                "98758588-pRnp7Qndq4faaMaNXRFDctD3acg56YL0Dugpu4VEf",
                "68iikmgVPgyzhl3tjP2kSKzuW2g0R1CVKXuIHPUYDpvqc"
        );
    }

    public int subscribe(/*Filter filter, UpdatesConsumer consumer*/) {
        // TODO: store filters and consumers, close connection and reopen if a new filter is added
        synchronized (this.listeners) {
            // TODO: UpdatesConsumer
            this.listeners.add(new TwitterListener(/*consumer*/));

            // TODO: reconnect if a new TwitterListener is added
            //if (this.listeners.size() == 1) {
                // TODO: generate twitter filters from Filter
               // List<Long> followings = Lists.newArrayList(1234L, 566788L);
                List<String> terms = Lists.newArrayList("argentina");
                List<String> langs = Lists.newArrayList("en");

                StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
              //  endpoint.followings(followings);
                endpoint.trackTerms(terms);
                endpoint.languages(langs);

                BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);

                ClientBuilder builder = new ClientBuilder()
                        .name("Twitter client for user "+this.userID)
                        .hosts(this.hosts)
                        .authentication(this.authentication)
                        .endpoint(endpoint)
                        .processor(new StringDelimitedProcessor(msgQueue));

                Client hosebirdClient = builder.build();
                // Attempts to establish a connection.
                //hosebirdClient.connect();

                this.t4jClient = new Twitter4jStatusClient(hosebirdClient, msgQueue, this.listeners, Executors.newCachedThreadPool());
                this.t4jClient.connect();

                // TODO: config for amount of threads
                this.t4jClient.process(); // creates a thread for processing of raw messages
            //}
        }
        return 0;
    }

    public void unsubscribe(int subscriberID) {
        // TODO: UpdatesConsumer
        // TODO: stop connection if there are no more listeners, close and reopen if a filter is not needed anymore
        synchronized (this.listeners) {
            // Guaranteed to work since TwitterListener overrides equals(Object)
            // this.listeners.remove(consumer);
            if(this.listeners.size() == 0)
                this.t4jClient.stop();
        }
    }
}
