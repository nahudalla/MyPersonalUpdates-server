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

public class TwitterUserLookupByIDRequestProcessor implements ProviderRequestProcessor {
    @Override
    public void process(User user, Request request, Response response)
            throws RequestProcessingException, SealedException, DBException {
        JsonElement element = request.getBodyItem("id");

        if(element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        TwitterProvider provider = TwitterProvider.getInstance();

        String accessToken = user.getAttribute(provider, "accessToken");
        String accessTokenSecret = user.getAttribute(provider, "accessTokenSecret");

        if(accessToken == null || accessTokenSecret == null)
            throw new RequestProcessingException(response.set(Handler.FORBIDDEN_RESPONSE));

        Twitter twitter = provider.getTwitterInstance();

        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        try {
            response.setType("TwitterUserName")
                    .set("username",twitter.users().showUser(element.getAsLong()).getScreenName())
                    .set("id", element.getAsLong())
                    .close();
        } catch (TwitterException e) {
            if(e.exceededRateLimitation() || e.isCausedByNetworkIssue())
                throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), e);

            if(e.resourceNotFound())
                throw new RequestProcessingException(response.set(
                        Handler.NOT_FOUND_RESPONSE
                ).setType("TwitterUserNotFound"));

            response.set(Handler.INTERNAL_ERROR_RESPONSE)
                    .setType("TwitterUserLookupError");

            if(e.isErrorMessageAvailable())
                response.setMessage(e.getErrorMessage()).close();
            else
                response.setMessage("No se pudo buscar el usuario solicitado.").close();

            throw new RequestProcessingException(response);
        }
    }
}

