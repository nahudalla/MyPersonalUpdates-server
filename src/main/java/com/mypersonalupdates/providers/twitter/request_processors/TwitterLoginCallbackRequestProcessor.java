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
import org.eclipse.jetty.http.HttpStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Esta clase representa una acción que el usuario puede ejecutar
 * en el proveedor de Twitter. Particularmente, se encarga de
 * recibir tokens de inicio de sesión proporcionados por Twitter
 * y de chequear su validez para así obtener los tokens de acceso
 * a Twitter del usuario que solicitó la operación.
 */
public final class TwitterLoginCallbackRequestProcessor implements ProviderRequestProcessor {
    @Override
    public void process(User user, Request request, Response response) throws RequestProcessingException, SealedException, DBException {
        TwitterProvider provider = TwitterProvider.getInstance();
        Twitter twitter = provider.getTwitterInstance();

        String oauth_token = null;
        String oauth_verifier = null;

        RequestToken requestToken = null;

        /* Chequeo de la existencia y formato de los datos recibidos */

        JsonElement e1 = null, e2 = null;

        if(request.hasBody()) {
            e1 = request.getBody().get("oauth_token");
            e2 = request.getBody().get("oauth_verifier");
        }

        if(e1 != null && e2 != null) {
            try {
                oauth_token = e1.getAsString();
                oauth_verifier = e2.getAsString();
            } catch (Exception e) {
                throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE), e);
            }
        }

        /* Chequeo para saber si el usuario que esta actualmente logueado es quien solicito el inicio de sesion */

        if(oauth_token != null && oauth_verifier != null) {
            String token = user.getAttribute(provider, "accessRequestToken");
            String tokenSecret = user.getAttribute(provider, "accessRequestTokenSecret");

            if(token != null && tokenSecret != null && token.equals(oauth_token))
                requestToken = new RequestToken(token, tokenSecret);
        }

        if(requestToken == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        /* Intercambio de tokens con Twitter para obtener los de acceso a la cuenta del usuario */

        AccessToken accessToken;
        try {
            accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
        } catch (TwitterException e) {
            if(e.exceededRateLimitation() || e.isCausedByNetworkIssue() || e.resourceNotFound())
                throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), e);

            response.set(Handler.FORBIDDEN_RESPONSE)
                    .setType("TwitterTokenExchangeError");

            if(e.isErrorMessageAvailable())
                response.setMessage(e.getErrorMessage());
            else
                response.setMessage("Tokens de acceso a Twitter inválidos.");

            throw new RequestProcessingException(response);
        }

        /* Almacenamiento en la base de datos de la información recibida */

        user.setAttribute(provider, "accessToken", accessToken.getToken());
        user.setAttribute(provider, "accessTokenSecret", accessToken.getTokenSecret());

        user.setAttribute(provider, "userID", String.valueOf(accessToken.getUserId()));

        if(accessToken.getScreenName() != null)
            user.setAttribute(provider, "screenName", accessToken.getScreenName());

        /* Confirmación de la operación al usuario */

        response.setStatus(HttpStatus.Code.OK)
                .clear()
                .setType("TwitterLoginCallback")
                .close();
    }
}
