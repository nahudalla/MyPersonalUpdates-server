package com.mypersonalupdates.providers.twitter;

import com.mypersonalupdates.Update;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TwitterUpdate implements Update {
    // TODO: implements Update

    private final Status status;

    public TwitterUpdate(Status status) {
        this.status = status;
    }

    @Override
    public Integer getProviderID() {
        // TODO: update diagram
        // TODO: get provider id from somewhere (perhaps unique (on db) for each provider?)
        return 0;
    }

    // TODO: DO NOT USE SOURCE, username instead
    @Override
    public String getSource() {
        return this.status.getUser().getScreenName();
    }

    // TODO: Update with dynamic attributes

    @Override
    public String getText() {
        return this.status.getText();
    }

    @Override
    public String getID() {
        Long l = this.status.getId();
        return l.toString();
    }

    @Override
    public Date getTimestamp() {
        return this.status.getCreatedAt();
    }

    @Override
    public List<String> getLinks() {
        URLEntity[] urlEntities = this.status.getURLEntities();
        List<String> urls = new ArrayList<>(urlEntities.length);
        for(URLEntity entity : urlEntities)
            urls.add(entity.getExpandedURL());
        return urls;
    }

    @Override
    public List<String> getMultimedia() {
        // TODO: implement
        return null;
    }

    @Override
    public List<String> getHashtags() {
        // TODO: implement
        return null;
    }

    @Override
    public List<String> getMentions() {
        // TODO: implement
        return null;
    }

    @Override
    public Integer getLikesCount() {
        return this.status.getFavoriteCount();
    }

    @Override
    public Integer getSharedCount() {
        return this.status.getRetweetCount();
    }

    @Override
    public boolean isOwnUpdate() {
        return !this.status.isRetweet();
    }

    @Override
    public Update getOriginalUpdate() {
        return new TwitterUpdate(this.status.getRetweetedStatus());
    }
}
