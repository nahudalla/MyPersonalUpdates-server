package com.mypersonalupdates.webserver.handlers.providers;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import java.util.Map;

/**
 * Esta clase se encarga de procesar una petición de realizar
 * una acción con un proveedor específico.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class ProviderActionHandler<T> extends ProviderValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        String name = request.getPathParam("actionName");
        Map<String, ProviderRequestProcessor> processors = name == null ? null : super.getProvider(request).getActions();
        ProviderRequestProcessor processor = processors == null ? null : processors.get(name);

        if(processor == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        processor.process(super.getAuthenticatedUser(request), request, response);
    }
}
