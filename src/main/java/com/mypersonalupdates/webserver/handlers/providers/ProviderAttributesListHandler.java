package com.mypersonalupdates.webserver.handlers.providers;

import com.google.gson.reflect.TypeToken;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.responses.ResponseData;
import com.mypersonalupdates.webserver.responses.builders.ProviderAttributeResponseBuilder;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * listar todos los atributos que posee una actualización de
 * un proveedor.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class ProviderAttributesListHandler<T> extends ProviderValidator<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Type type = new TypeToken<List<ResponseData>>(){}.getType();
        List<ResponseData> list = new LinkedList<>();

        for (UpdatesProviderAttribute attr : super.getProvider(request).getAttributes())
            list.add(
                    new ProviderAttributeResponseBuilder(attr)
                            .includeID()
                            .includeProviderID()
                            .includeName()
                            .includeDescription()
                            .includeFilterNotes()
                            .includeMultivalued()
                            .build()
            );

        response.setType("ProviderAttributesList")
                .set("attributes", list, type)
                .close();
    }
}
