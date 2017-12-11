package com.mypersonalupdates.providers.twitter.request_processors;

import com.google.gson.JsonElement;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.twitter.TwitterProvider;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * Esta clase representa una acción que el usuario puede ejecutar
 * en el proveedor de Twitter. Particularmente, se encarga de
 * verificar si el usuario tiene vinculada una cuenta de Twitter.
 */
public final class TwitterLoginCheckProcessor implements ProviderRequestProcessor {
    @Override
    public void process(User user, Request request, Response response)
            throws RequestProcessingException, SealedException, DBException {

        TwitterProvider provider = TwitterProvider.getInstance();

        String accessToken = user.getAttribute(provider, "accessToken");
        String accessTokenSecret = user.getAttribute(provider, "accessTokenSecret");

        response.setType("TwitterLoginCheck")
                .set("loggedIn", accessToken != null && accessTokenSecret != null)
                .close();
    }
}
