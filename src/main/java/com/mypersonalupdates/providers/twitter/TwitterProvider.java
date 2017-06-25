package com.mypersonalupdates.providers.twitter;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;

import java.util.*;

// TODO: implements UpdatesProvider
public class TwitterProvider {
    public static void main(String []args) throws DBException {
        TwitterProvider provider = new TwitterProvider();
        User user = User.fromUsername("nahuel");
        provider.subscribe(user);
    }

    //TODO: change to Hashtable<Integer, UpdatesConsumer>
    private final Hashtable<Integer, TwitterStream> streams = new Hashtable<>();

    // TODO: get provider ID from somewhere? (UpdatesProviderManager.getNewProviderId()?)
    // TODO: fix diagram
    public int getId() {return 0;}

    public List<String> getAcceptedFilterFields() {
        // TODO: return accepted fields
        System.err.println("TwitterProvider.getAcceptedFilterFields() not implemented.");
        return null;
    }

    public int subscribe(User user/*, Filter filter, UpdatesConsumer consumer*/) {
        // TODO: use filter and consumer properly
        // TODO: update diagram
        TwitterStream stream = this.streams.computeIfAbsent(user.getId(), k -> new TwitterStream(user));

        return stream.subscribe(/*filter, consumer*/);
    }

    public void unsubscribe(User user, int subscriberID) {
        TwitterStream stream = this.streams.get(user.getId());
        if(stream != null)
            stream.unsubscribe(subscriberID);
    }
}
