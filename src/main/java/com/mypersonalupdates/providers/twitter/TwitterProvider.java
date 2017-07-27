package com.mypersonalupdates.providers.twitter;

import com.mypersonalupdates.Config;
import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdatesProviderActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.users.User;

import java.util.Collection;
import java.util.Hashtable;

public class TwitterProvider implements UpdatesProvider {
    private static final int PROVIDER_ID = Config.get().getInt("providers.twitter.databaseProviderID");

    static {
        UpdatesProvidersManager.getInstance().addProvider(new TwitterProvider());
    }

    private final UpdatesProviderActions dbActions;

    private TwitterProvider() {
        this.dbActions = DBConnection.getInstance().onDemand(UpdatesProviderActions.class);
    }

    public static TwitterProvider getInstance() {
        return (TwitterProvider) UpdatesProvidersManager.getInstance().getProvider(TwitterProvider.PROVIDER_ID);
    }

    private final Hashtable<Integer, TwitterStream> streams = new Hashtable<>();

    // TODO: arreglar diagrama
    @Override
    public Integer getID() {
        return TwitterProvider.PROVIDER_ID;
    }

    @Override
    public String getName() {
        return this.dbActions.getName(this.getID());
    }

    @Override
    public String getDescription() {
        return this.dbActions.getDescription(this.getID());
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes() {
        return this.dbActions.getAttributes(this.getID());
    }

    @Override
    // TODO: actualizar signature en diagrama
    public Long subscribe(User user, Filter filter, UpdatesConsumer consumer) {
        TwitterStream stream;

        synchronized (this.streams) {
            stream = this.streams.get(user.getID());
            if(stream == null) {
                try {
                    stream = new TwitterStream(this, user);
                } catch (DBException e) {
                    e.printStackTrace();
                    return null;
                }

                this.streams.put(user.getID(), stream);
            }
        }

        return stream.subscribe(filter, consumer);
    }

    @Override
    public boolean unsubscribe(User user, Long subscriberID) {
        TwitterStream stream;

        synchronized (this.streams) {
            stream = this.streams.get(user.getID());
        }

        return stream != null && stream.unsubscribe(subscriberID);
    }

    @Override
    public void stop() {
        synchronized (this.streams) {
            for (TwitterStream stream : this.streams.values())
                stream.stop();

            this.streams.clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof TwitterProvider);
    }
}
