package com.mypersonalupdates.webserver.handlers.update;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.gson.reflect.TypeToken;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.Log;
import com.mypersonalupdates.log.Restrictions;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import com.mypersonalupdates.webserver.responses.ResponseData;
import com.mypersonalupdates.webserver.responses.builders.UpdateResponseBuilder;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * buscar entre las actualizaciones que hay en el sistema
 * aquellas que cumplan con un criterio de filtrado y restricciones
 * de búsqueda.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public class UpdatesSearchHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, com.mypersonalupdates.webserver.Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Filter filter = request.getBodyItem("filter", Filter.class);

        if (filter == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        Restrictions restrictions = request.getBodyItem("restrictions", Restrictions.class);

        Collection<Update> updates;
        try {
            updates = Log.getInstance().getUpdates(filter, restrictions);
        } catch (Exception e) {
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), e);
        }

        Type type = new TypeToken<Collection<Update>>(){}.getType();

        response.setType("UpdatesSearchResult")
                .set("updates", Collections2.transform(
                        updates,
                        new Function<Update, ResponseData>() {
                            @Nullable
                            @Override
                            public ResponseData apply(@Nullable Update input) {
                                if (input == null) return null;
                                try {
                                    return new UpdateResponseBuilder(input)
                                            .includeProviderID()
                                            .includeTimestamp()
                                            .includeAttributes()
                                            .build();
                                } catch (DBException e) {
                                    e.printStackTrace();
                                    // TODO: log
                                }
                                return null;
                            }
                        }), type
                ).close();

        try {
            filter.remove();
        }catch (Exception e) {
            // TODO: log
        }
    }
}
