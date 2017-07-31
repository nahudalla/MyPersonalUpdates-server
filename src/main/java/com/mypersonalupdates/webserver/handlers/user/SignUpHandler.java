package com.mypersonalupdates.webserver.handlers.user;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import org.eclipse.jetty.http.HttpStatus;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * registrar un usuario nuevo en el sistema.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class SignUpHandler<T> extends Handler<T> {
    private static final Response USERNAME_NOT_AVAILABLE_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.CONFLICT)
            .setType("SignupUsernameNotAvailable")
            .setMessage("El nombre de usuario especificado no está disponible.")
            .build();
    private static final Response USER_CREATED_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.CREATED)
            .setType("SignUpOK")
            .setMessage("Usuario creado correctamente.")
            .build();

    private final CredentialsSyntaxValidator<T> syntaxValidator = new CredentialsSyntaxValidator<>();

    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        this.syntaxValidator.processRequest(request, response);

        User user = User.getFromUsername(this.syntaxValidator.getUsername(request));

        if (user != null)
            throw new RequestProcessingException(response.set(USERNAME_NOT_AVAILABLE_RESPONSE));

        user = User.createNew(this.syntaxValidator.getUsername(request), this.syntaxValidator.getPassword(request));

        if (user != null) {
            response.set(SignUpHandler.USER_CREATED_RESPONSE).close();
            return;
        }

        throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), new AssertionError());
    }
}
