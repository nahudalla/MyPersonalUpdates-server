package com.mypersonalupdates.providers.twitter;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterListener implements StatusListener {
    // TODO: use UpdatesConsumer when implemented

    /*UpdatesConsumer consumer;*/

    public TwitterListener(/*UpdatesConsumer consumer*/) {
        // this.consumer = consumer;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO: replace with
        // return this.consumer.equals(obj);
        return super.equals(obj);
    }

    @Override
    public void onStatus(Status status) {
        TwitterUpdate update = new TwitterUpdate(status);
        System.out.println(update.getSource()+": "+update.getText());
        // TODO: use UpdatesConsumer when implemented
        // this.consumer.handleUpdate(update);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        // TODO: handle properly
        System.out.println("status deleted: "+statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        // TODO: handle properly
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        // TODO: handle properly
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        // TODO: handle properly
    }

    @Override
    public void onException(Exception e) {
        // TODO: handle properly
    }
}
