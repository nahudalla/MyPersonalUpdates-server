package com.mypersonalupdates.webserver.handlers.auth;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.JWT;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.responses.builders.JWTResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición de
 * actualización de token de acceso de un usuario.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class TokenRefreshHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        JWT token = super.getAuthenticationToken(request);

        if(token.getTimeToRefresh() <= 0) {
            token = token.renew();

            request.setData(token);

            response.set(
                    new JWTResponseBuilder(token)
                            .includeType()
                            .includeToken()
                            .includeTimeToRefresh()
                            .build()
            ).close();
        }else
            response.set(
                    new JWTResponseBuilder(token)
                            .includeType()
                            .includeTimeToRefresh()
                            .build()
            ).close();
    }
}
