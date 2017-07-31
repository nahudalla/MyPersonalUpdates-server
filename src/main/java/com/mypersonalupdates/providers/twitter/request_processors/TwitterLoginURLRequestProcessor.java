package com.mypersonalupdates.providers.twitter.request_processors;

import com.mypersonalupdates.Config;
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
import twitter4j.auth.RequestToken;

/**
 * Esta clase representa una acción que el usuario puede ejecutar
 * en el proveedor de Twitter. Particularmente, se encarga de
 * devolver una URL de twitter.com que le permite al usuario
 * iniciar sesión en el servicio para luego recibir los tokens
 * de acceso mediante {@link TwitterLoginCallbackRequestProcessor}.
 */
public final class TwitterLoginURLRequestProcessor implements ProviderRequestProcessor {
    private static final String CALLBACK_URL = Config.get().getString("providers.twitter.OAuthCallbackURL");

    @Override
    public void process(User user, Request request, Response response) throws RequestProcessingException, SealedException, DBException {
        TwitterProvider provider = TwitterProvider.getInstance();
        Twitter twitter = provider.getTwitterInstance();

        RequestToken requestToken;
        try {
            requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
        } catch (TwitterException e) {
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), e);
        }

        user.setAttribute(provider, "accessRequestToken", requestToken.getToken());
        user.setAttribute(provider, "accessRequestTokenSecret", requestToken.getTokenSecret());

        response.setType("TwitterLoginURL")
                .set("url", requestToken.getAuthorizationURL())
                .close();
    }
}
