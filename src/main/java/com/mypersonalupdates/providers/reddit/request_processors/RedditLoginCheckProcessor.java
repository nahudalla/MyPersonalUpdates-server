package com.mypersonalupdates.providers.reddit.request_processors;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.reddit.RedditUserAuthenticator;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

public class RedditLoginCheckProcessor implements ProviderRequestProcessor {
    @Override
    public void process(User user, Request request, Response response) throws RequestProcessingException, SealedException, DBException {
        RedditUserAuthenticator redditUserAuthenticator = new RedditUserAuthenticator(user);

        String userToken = redditUserAuthenticator.getAuthToken();

        response
                .setType("RedditLoginCheck")
                .set("loggedIn", userToken != null)
                .close();
    }
}
