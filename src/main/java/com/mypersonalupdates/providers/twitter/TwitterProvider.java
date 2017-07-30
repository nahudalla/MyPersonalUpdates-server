package com.mypersonalupdates.providers.twitter;

import com.mypersonalupdates.Config;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdatesProviderActions;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.providers.twitter.request_processors.TwitterLoginCallbackRequestProcessor;
import com.mypersonalupdates.providers.twitter.request_processors.TwitterLoginURLRequestProcessor;
import com.mypersonalupdates.providers.twitter.request_processors.TwitterUserLookupRequestProcessor;
import com.mypersonalupdates.users.User;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Esta clase representa al proveedor de Twitter.com y
 * sirve de interfaz para todas las operaciones que se
 * pueden realizar con dicho proveedor.
 */
public final class TwitterProvider implements UpdatesProvider {
    private static final Long PROVIDER_ID;
    private static final UpdatesProviderActions DB_ACTIONS;
    private static final TwitterFactory TWITTER_FACTORY;
    private static final Map<String, ProviderRequestProcessor> REQUEST_PROCESSORS = new Hashtable<>();

    static {
        // Inicializacion de constantes
        PROVIDER_ID = Config.get().getLong("providers.twitter.databaseProviderID");
        DB_ACTIONS = DBConnection.getInstance().onDemand(UpdatesProviderActions.class);
        TWITTER_FACTORY = new TwitterFactory();
    }

    public Twitter getTwitterInstance() {
        Twitter twitter = TWITTER_FACTORY.getInstance();

        twitter.setOAuthConsumer(
                Config.get().getString("providers.twitter.consumerKey"),
                Config.get().getString("providers.twitter.consumerSecret")
        );

        return twitter;
    }

    private TwitterProvider() {}

    public static TwitterProvider getInstance() {
        return (TwitterProvider) UpdatesProvidersManager.getInstance().getProvider(TwitterProvider.PROVIDER_ID);
    }

    private final Hashtable<Long, TwitterStream> streams = new Hashtable<>();

    // TODO: arreglar diagrama
    @Override
    public Long getID() {
        return TwitterProvider.PROVIDER_ID;
    }

    @Override
    public String getName() {
        return DB_ACTIONS.getName(this.getID());
    }

    @Override
    public String getDescription() {
        return DB_ACTIONS.getDescription(this.getID());
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes() {
        return DB_ACTIONS.getAttributes(this.getID());
    }

    @Override
    // TODO: actualizar signature en diagrama
    public Long subscribe(User user, Filter filter, UpdatesConsumer consumer) throws UserNotLoggedInToProviderException {
        TwitterStream stream;

        synchronized (this.streams) {
            stream = this.streams.get(user.getID());
            if (stream == null) {
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

    public static void setup() {
        // Registro del proveedor en el manager
        UpdatesProvidersManager.getInstance().addProvider(new TwitterProvider());

        // Registro de las acciones disponibles
        REQUEST_PROCESSORS.put("loginURL", new TwitterLoginURLRequestProcessor());
        REQUEST_PROCESSORS.put("login", new TwitterLoginCallbackRequestProcessor());
        REQUEST_PROCESSORS.put("userLookup", new TwitterUserLookupRequestProcessor());
    }

    @Override
    public Map<String, ProviderRequestProcessor> getActions() {
        return REQUEST_PROCESSORS;
    }

    @Override
    public int hashCode() {
        return PROVIDER_ID.hashCode();
    }
}
