package com.mypersonalupdates.providers.reddit;

import com.mypersonalupdates.Config;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.users.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RedditSubscription {
    private static final Long SOURCE_USERNAME_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.source_username");
    private static final Long SOURCE_SUBREDDITNAME_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.source_subredditname");

    private static long currentID = 0;

    private List<RedditUpdatesFetcher> fetchers = new LinkedList<>();
    private final long ID = currentID++;

    public RedditSubscription(User user, Filter filter, UpdatesConsumer consumer) throws DBException, UserNotLoggedInToProviderException {
        UpdatesProviderAttribute username_attribute = UpdatesProviderAttribute.create(RedditProvider.getInstance(), SOURCE_USERNAME_ATTR_ID);
        UpdatesProviderAttribute subreddit_attribute = UpdatesProviderAttribute.create(RedditProvider.getInstance(), SOURCE_SUBREDDITNAME_ATTR_ID);

        Collection<String> username_values = filter.getValues(username_attribute);
        Collection<String> subreddit_values = filter.getValues(subreddit_attribute);

        RedditUserAuthenticator authenticator = new RedditUserAuthenticator(user);

        if (authenticator.getAuthToken() == null){
            throw new UserNotLoggedInToProviderException();
        }

        for (String username : username_values){
            RedditResource username_resource = new RedditUserResource(authenticator, username);
            RedditUpdatesFetcher fetcher = new RedditUpdatesFetcher(filter, consumer, username_resource);
            this.fetchers.add(fetcher);
            (new Thread(fetcher)).start();
        }

        for (String subreddit : subreddit_values){
            RedditResource subreddit_resource = new RedditUserResource(authenticator, subreddit);
            RedditUpdatesFetcher fetcher = new RedditUpdatesFetcher(filter, consumer, subreddit_resource);
            this.fetchers.add(fetcher);
            (new Thread(fetcher)).start();
        }
    }

    public long getID() {
        return this.ID;
    }

    public void stop(){
        for (RedditUpdatesFetcher fetcher : this.fetchers) {
            fetcher.stop();
        }
    }
}
