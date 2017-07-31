package com.mypersonalupdates.webserver.handlers.providers;

import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;

/**
 * Esta clase se encarga de procesar una parte de una petición
 * que necesita un proveedor existente. Particularmente se
 * encarga de chequear que el proveedor especificado en la
 * petición exista.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public abstract class ProviderValidator<T> extends UserAuthHandler<T> {
    protected UpdatesProvider getProvider(Request request) {
        return (UpdatesProvider) request.getData(UpdatesProvider.class);
    }

    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        if(this.getProvider(request) != null)
            return;

        Long provID;
        try {
            String strProvID = request.getPathParam("providerID");
            provID = Long.valueOf(strProvID);
        } catch (NumberFormatException e) {
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE), e);
        }

        UpdatesProvider provider = UpdatesProvidersManager.getInstance().getProvider(provID);

        if (provider == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        request.setData(provider, UpdatesProvider.class);
    }
}
