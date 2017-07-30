package com.mypersonalupdates.webserver.handlers.category;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

/**
 * Esta clase se encarga de procesar una petición de
 * eliminar una categoría.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CategoryRemoveHandler<T> extends CategoryValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        if(super.getCategory(request).remove())
            response.set(Handler.DEFAULT_RESPONSE).close();
        else
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE));
    }
}
