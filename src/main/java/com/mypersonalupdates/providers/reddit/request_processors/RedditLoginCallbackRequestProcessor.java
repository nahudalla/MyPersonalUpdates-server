package com.mypersonalupdates.providers.reddit.request_processors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.reddit.RedditClient;
import com.mypersonalupdates.providers.reddit.RedditProvider;
import com.mypersonalupdates.providers.reddit.RedditUserAuthenticator;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import okhttp3.ResponseBody;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.time.Instant;

public class RedditLoginCallbackRequestProcessor implements ProviderRequestProcessor{

    // Nos solicita que el usuario nuestro logeado, lo vinculemos con su cuenta de Reddit

    @Override
    public void process(User user, Request request, Response response) throws RequestProcessingException, SealedException {

        String code = null;

        if (request.hasBody()) {
            JsonElement element = request.getBody().get("code");

            if (element != null)
                try{
                    code = element.getAsString();
                } catch (Exception e) {
                    throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE), e);
                }
        }

        if (code == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        RedditUserAuthenticator authenticator = new RedditUserAuthenticator(user);

        if(!authenticator.setupAuthToken(code))
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE));

        response.setStatus(HttpStatus.Code.OK)
                .clear()
                .setType("RedditLoginCallback")
                .close();
    }
}
