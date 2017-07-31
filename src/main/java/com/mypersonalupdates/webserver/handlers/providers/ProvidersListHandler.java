package com.mypersonalupdates.webserver.handlers.providers;

import com.google.gson.reflect.TypeToken;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import com.mypersonalupdates.webserver.responses.ResponseData;
import com.mypersonalupdates.webserver.responses.builders.ProviderResponseBuilder;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * mostrar todos los proveedores disponibles.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class ProvidersListHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Type type = new TypeToken<List<ResponseData>>(){}.getType();
        List<ResponseData> providers = new LinkedList<>();

        for (UpdatesProvider provider : UpdatesProvidersManager.getInstance().getProviders()) {
            providers.add(
                    new ProviderResponseBuilder(provider)
                            .includeID()
                            .includeName()
                            .includeDescription()
                            .build()
            );
        }

        response.setType("ProvidersList")
                .set("providers", providers, type)
                .close();
    }
}
