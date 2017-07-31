package com.mypersonalupdates.webserver.handlers.providers;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.responses.builders.ProviderResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * mostrar todos los datos de un proveedor.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class ProviderHandler<T> extends ProviderValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        response.set(
                new ProviderResponseBuilder(super.getProvider(request))
                        .includeType()
                        .includeID()
                        .includeName()
                        .includeDescription()
                        .includeActions()
                        .build()
        ).close();
    }
}
