package com.mypersonalupdates.webserver.handlers.providers;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.responses.builders.ProviderAttributeResponseBuilder;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * mostrar todos los datos de un atributo asociado a un proveedor.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class ProviderAttributeHandler<T> extends ProviderValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Long attrID;

        try {
            String strAttrID = request.getPathParam("attrID");

            attrID = Long.valueOf(strAttrID);
        } catch (NumberFormatException e) {
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE), e);
        }

        UpdatesProviderAttribute attribute = UpdatesProviderAttribute.create(
                super.getProvider(request),
                attrID
        );

        if (attribute == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        response.set(
                new ProviderAttributeResponseBuilder(attribute)
                        .includeType()
                        .includeID()
                        .includeProviderID()
                        .includeName()
                        .includeDescription()
                        .includeFilterNotes()
                        .includeMultivalued()
                        .build()
        ).close();
    }
}
