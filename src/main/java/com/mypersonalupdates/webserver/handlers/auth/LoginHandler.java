package com.mypersonalupdates.webserver.handlers.auth;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.JWT;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.user.CredentialsSyntaxValidator;
import com.mypersonalupdates.webserver.responses.builders.JWTResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición de
 * inicio de sesión de un usuario.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class LoginHandler<T> extends Handler<T> {
    private static final Response INVALID_CREDENTIALS_RESPONSE = new Response.Builder(Handler.FORBIDDEN_RESPONSE)
            .setType("InvalidLoginCredentials")
            .setMessage("Las credenciales especificadas no son correctas.")
            .build();

    private final CredentialsSyntaxValidator<T> syntaxValidator = new CredentialsSyntaxValidator<>();

    @Override
    protected void processRequest(Request request, Response response) throws DBException, SealedException, RequestProcessingException {
        this.syntaxValidator.processRequest(request, response);

        User user = User.getFromCredentials(
                this.syntaxValidator.getUsername(request),
                this.syntaxValidator.getPassword(request)
        );

        if (user == null)
            throw new RequestProcessingException(response.set(INVALID_CREDENTIALS_RESPONSE));

        JWT token = new JWT(user.getID());

        response.set(
                new JWTResponseBuilder(token)
                        .includeToken()
                        .includeTimeToRefresh()
                        .build()
        ).close();
    }
}
