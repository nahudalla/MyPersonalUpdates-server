package com.mypersonalupdates.providers.twitter;

import twitter4j.Status;

public class TwitterUpdate {
    // TODO: implements Update

    private String username, text;

    public TwitterUpdate(Status status) {
        this.username = status.getUser().getName();
        this.text = status.getText();
    }

    public int getProviderID() {
        // TODO: update diagram
        // TODO: get provider id from somewhere (perhaps unique (on db) for each provider?)
        return 0;
    }

    // TODO: DO NOT USE SOURCE, user -> name instead

    public String getSource() {
        return this.username;
    }

    // TODO: Update with dynamic attributes

    public String getText() {
        return this.text;
    }
}
