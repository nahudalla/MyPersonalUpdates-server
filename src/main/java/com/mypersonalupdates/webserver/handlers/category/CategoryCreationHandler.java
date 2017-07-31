package com.mypersonalupdates.webserver.handlers.category;

import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Iterator;
import java.util.Set;

/**
 * Esta clase se encarga de procesar una petición de
 * creación de una categoría.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CategoryCreationHandler<T> extends UserAuthHandler<T> {
    private static final Response CATEGORY_EXISTS_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.CONFLICT)
            .setType("CategoryAlreadyExists")
            .setMessage("Ya existe una categoría con el nombre especificado.")
            .build();
    private static final Response CATEGORY_CREATED_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.CREATED)
            .setType("CategoryCreated")
            .setMessage("La categoría se creó correctamente.")
            .build();

    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        /* Chequeo que el nombre de la categoría nueva esté en la petición */

        String pathName = request.getPathParam("categoryName");

        if(pathName == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        String bodyName = CategoryCommons.getBodyName(request);

        /* Chequeo que el nombre, los proveedores y el filtro sean correctos */

        boolean nameOK = bodyName != null && bodyName.equals(pathName);

        Set<UpdatesProvider> providers = !nameOK ? null : CategoryCommons.getProviders(request);

        Filter filter = providers == null ? null : request.getBodyItem("filter", Filter.class);

        if(filter == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        /* Creo la categoría nueva con el filtro dado */

        Category category;
        try {
            category = Category.create(
                    super.getAuthenticatedUser(request),
                    pathName,
                    filter
            );
        } catch (UserNotLoggedInToProviderException e) {
            throw new RequestProcessingException(
                    response.setType("UserNotLoggedInToProvider")
                            .merge(Handler.FORBIDDEN_RESPONSE),
                    e
            );
        }

        if(category == null) {
            // Si no se pudo crear la categoría porque ya existe, eliminar
            // los posibles filtros creados de la base de datos.
            filter.remove();
            throw new RequestProcessingException(response.set(CATEGORY_EXISTS_RESPONSE));
        }

        /* Le agrego los proveedores a la categoría nueva */

        category.suspendRealTimeSubscriptions();

        boolean ok;
        try {
            Iterator<UpdatesProvider> it = providers.iterator();
            ok = true;
            while (it.hasNext() && ok) {
                ok = category.addProvider(it.next());
            }
        } catch (UserNotLoggedInToProviderException e) {

            throw new AssertionError(e);
        } finally {
            try {
                category.resumeRealTimeSubscriptions();
            } catch (UserNotLoggedInToProviderException e) {

                category.remove();
                //noinspection ThrowFromFinallyBlock
                throw new RequestProcessingException(
                        response.setType("UserNotLoggedInToProvider")
                                .merge(Handler.FORBIDDEN_RESPONSE),
                        e
                );
            }
        }

        if(!ok) {
            // Si no se pudieron agregar los proveedores
            category.remove();
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE));
        }

        // Si no hubo errores

        response.set(CATEGORY_CREATED_RESPONSE).close();
    }
}
