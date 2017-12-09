package com.mypersonalupdates.providers.reddit;

import com.mypersonalupdates.Config;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdatesProviderActions;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.providers.reddit.request_processors.RedditLoginCallbackRequestProcessor;
import com.mypersonalupdates.providers.reddit.request_processors.RedditLoginCheckProcessor;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.users.User;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class RedditProvider implements UpdatesProvider {
    private static final Long PROVIDER_ID = Config.get().getLong("providers.reddit.databaseProviderID");
    private static final UpdatesProviderActions DB_ACTIONS = DBConnection.getInstance().onDemand(UpdatesProviderActions.class);
    private static final Map<String, ProviderRequestProcessor> REQUEST_PROCESSORS = new Hashtable<>();

    private final Map<Long, RedditSubscription> subscriptions = new Hashtable<>();
    private RedditProvider() {}

    public static RedditProvider getInstance() {
        return (RedditProvider) UpdatesProvidersManager.getInstance().getProvider(RedditProvider.PROVIDER_ID);
    }

    @Override
    public Long getID() {
        return RedditProvider.PROVIDER_ID;
    }

    @Override
    public String getName() {
        return RedditProvider.DB_ACTIONS.getName(this.getID());
    }

    @Override
    public String getDescription() {
        return RedditProvider.DB_ACTIONS.getDescription(this.getID());
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes() {
        return RedditProvider.DB_ACTIONS.getAttributes(this.getID());
    }

    @Override
    public Long subscribe(User user, Filter filter, UpdatesConsumer consumer) throws UserNotLoggedInToProviderException {
        try {
            RedditSubscription subscription = new RedditSubscription(user, filter, consumer);
            this.subscriptions.put(subscription.getID(), subscription);
            return subscription.getID();
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean unsubscribe(User user, Long subscriberID) {
        RedditSubscription subscription = this.subscriptions.remove(subscriberID);
        if (subscription != null) {
            subscription.stop();
            return true;
        }

        return false;
    }

    @Override
    public void stop() {
        for (RedditSubscription subscription : this.subscriptions.values()) {
            subscription.stop();
        }
        this.subscriptions.clear();
    }

    public static void setup(){
        UpdatesProvidersManager.getInstance().addProvider(new RedditProvider());
        RedditProvider.REQUEST_PROCESSORS.put("login", new RedditLoginCallbackRequestProcessor());
        RedditProvider.REQUEST_PROCESSORS.put("loginCheck", new RedditLoginCheckProcessor());

    }

    @Override
    public Map<String, ProviderRequestProcessor> getActions() {
        return RedditProvider.REQUEST_PROCESSORS;
    }
}
