package com.mypersonalupdates.webserver.handlers.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.JsonElement;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.JWT;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

/**
 * Esta clase se encarga de procesar una parte de una
 * petición de un usuario. En particular, se encarga
 * de chequear que el usuario se encuentra logueado
 * y que suministró un token de acceso válido.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public abstract class UserAuthHandler<T> extends Handler<T> {
    private static final Response INVALID_TOKEN_RESPONSE = new Response.Builder(Handler.FORBIDDEN_RESPONSE)
            .setType("InvalidAccessToken")
            .setMessage("Token de acceso invalido.")
            .build();

    protected JWT getAuthenticationToken(Request request) {
        return (JWT) request.getData(JWT.class);
    }

    protected User getAuthenticatedUser(Request request) {
        return (User) request.getData(User.class);
    }

    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        JWT token = this.getAuthenticationToken(request);
        User user = this.getAuthenticatedUser(request);

        if(token != null && user != null)
            return;

        String rawToken;
        try {
            String queryToken = request.hasQueryParams() ? request.getQueryParam("token") : null;
            JsonElement bodyToken = request.hasBody() ? request.getBodyItem("token") : null;

            rawToken = bodyToken == null ? queryToken : bodyToken.getAsString();
        }catch (Exception e) {
            throw new RequestProcessingException(response.set(INVALID_TOKEN_RESPONSE), e);
        }

        try {
            token = rawToken == null ? null : JWT.from(rawToken);
        } catch (JWTVerificationException e) {
            throw new RequestProcessingException(response.set(INVALID_TOKEN_RESPONSE), e);
        }

        user = ((token == null) || token.isExpired()) ? null : User.getFromID(token.getUserID());
        if(user == null)
            throw new RequestProcessingException(response.set(INVALID_TOKEN_RESPONSE));

        request.setData(token);
        request.setData(user);
    }
}
