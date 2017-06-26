package com.mypersonalupdates.providers.twitter;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.UpdatesConsumer;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.users.User;
import com.sun.istack.internal.NotNull;

import javax.annotation.Nonnull;
import java.util.*;

public class TwitterProvider implements UpdatesProvider {
    public static void main(String []args) {

        TwitterProvider provider = new TwitterProvider();

        User user = null;
        try {
            user = User.fromUsername("nahuel");
        } catch (DBException e) {
            System.err.println("Could not obtain user from database.");
        }

        provider.subscribe(user, new Filter(), update -> {
            if(update.isOwnUpdate())
                System.out.println("@" + update.getSource()+": "+update.getText());
            else {
                Update original = update.getOriginalUpdate();
                System.out.println("@" + update.getSource() + " retweeted @" + original.getSource() + ": " + original.getText());
            }
        });
    }

    private final Hashtable<Integer, TwitterStream> streams = new Hashtable<>();

    // TODO: get provider ID from somewhere? (UpdatesProviderManager.getNewProviderId()?)
    // TODO: fix diagram
    public Integer getID() {return 0;}

    public List<String> getAcceptedFilterFields() {
        // TODO: return accepted fields
        System.err.println("TwitterProvider.getAcceptedFilterFields() not implemented.");
        return null;
    }

    public Integer subscribe(User user, Filter filter, UpdatesConsumer consumer) {
        // TODO: update diagram
        TwitterStream stream = this.streams.computeIfAbsent(user.getId(), k -> new TwitterStream(user));

        return stream.subscribe(filter, consumer);
    }

    public boolean unsubscribe(User user, Integer subscriberID) {
        TwitterStream stream = this.streams.get(user.getId());
        return stream != null && stream.unsubscribe(subscriberID);
    }
}
