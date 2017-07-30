package com.mypersonalupdates.webserver.handlers.category;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.responses.builders.CategoryResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición de
 * mostrar los datos de una categoría específica.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CategoryHandler<T> extends CategoryValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        response.set(
                new CategoryResponseBuilder(super.getCategory(request))
                        .includeType()
                        .includeName()
                        .includeFilter()
                        .includeProviders()
                        .build()
        ).close();
    }
}
