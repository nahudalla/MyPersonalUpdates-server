package com.mypersonalupdates.webserver.handlers.user;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import com.mypersonalupdates.webserver.responses.builders.UserResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * mostrar la información de un usuario.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class UserInfoHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        response.set(
                new UserResponseBuilder(super.getAuthenticatedUser(request))
                        .includeType()
                        .includeID()
                        .includeUsername()
                        .build()
        ).close();
    }
}
