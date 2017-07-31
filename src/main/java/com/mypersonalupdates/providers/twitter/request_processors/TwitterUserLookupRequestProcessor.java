package com.mypersonalupdates.providers.twitter.request_processors;

import com.google.gson.JsonElement;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.twitter.TwitterProvider;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * Esta clase representa una acción que el usuario puede ejecutar
 * en el proveedor de Twitter. Particularmente, se encarga de
 * hacer una consulta en la API de Twitter para obtener el identificador
 * de un usuario a partir de su nombre de usuario.
 */
public final class TwitterUserLookupRequestProcessor implements ProviderRequestProcessor {
    @Override
    public void process(User user, Request request, Response response)
            throws RequestProcessingException, SealedException, DBException {
        JsonElement element = request.getBodyItem("username");

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
            response.setType("TwitterUserID")
                    .set("id",twitter.users().showUser(element.getAsString()).getId())
                    .set("username", element.getAsString())
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
