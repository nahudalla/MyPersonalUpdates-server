package com.mypersonalupdates.webserver.handlers.category;

import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Iterator;
import java.util.Set;

/**
 * Esta clase se encarga de procesar una petición de
 * actualizar los datos de una categoría.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CategoryUpdateHandler<T> extends UserAuthHandler<T> {
    private static final Response CATEGORY_EXISTS_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.CONFLICT)
            .setType("CategoryAlreadyExists")
            .setMessage("Ya existe una categoría con el nombre especificado.")
            .build();
    private static final Response CATEGORY_UPDATED_RESPONSE = new Response.Builder()
            .setStatus(HttpStatus.Code.OK)
            .setType("CategoryUpdated")
            .setMessage("La categoría se actualizó correctamente.")
            .build();

    @Override
    protected void processRequest(Request request, Response response)
            throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        String pathName = request.getPathParam("categoryName");

        Category category = pathName == null ? null : Category.create(super.getAuthenticatedUser(request), pathName);

        if(category == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        category.suspendRealTimeSubscriptions();

        try {
            this.addProviders(category, CategoryCommons.getProvidersToAdd(request));
            this.removeProviders(category, CategoryCommons.getProvidersToRemove(request));
            this.updateName(request, response, category, pathName, CategoryCommons.getBodyName(request));

            Filter filter = request.getBodyItem("filter", Filter.class);

            if (filter != null)
                category.setFilter(filter);
        } catch (UserNotLoggedInToProviderException e) {
            throw new RequestProcessingException(
                    response.setType("UserNotLoggedInToProvider")
                            .merge(Handler.FORBIDDEN_RESPONSE)
            );
        } finally {
            try {
                category.resumeRealTimeSubscriptions();
            } catch (UserNotLoggedInToProviderException e) {
                // ignore
            }
        }

        response.set(CATEGORY_UPDATED_RESPONSE).close();
    }

    private boolean removeProviders(Category category, Set<UpdatesProvider> providers) throws DBException, UserNotLoggedInToProviderException {
        if(providers == null)
            return true;

        Iterator<UpdatesProvider> it = category.getProviders().iterator();
        boolean ok = true;
        while (it.hasNext() && ok) {
            UpdatesProvider provider = it.next();
            if(providers.contains(provider))
                ok = category.removeProvider(it.next());
        }

        return ok;
    }

    private boolean addProviders(Category category, Set<UpdatesProvider> providers) throws DBException, UserNotLoggedInToProviderException {
        if(providers == null)
            return true;

        Iterator<UpdatesProvider> it = providers.iterator();
        boolean ok = true;
        while (it.hasNext() && ok) {
            ok = category.addProvider(it.next());
        }

        return ok;
    }

    private boolean updateName(Request request, Response response, Category category, String pathName, String bodyName)
            throws DBException, SealedException, RequestProcessingException {
        if (bodyName == null || pathName.equals(bodyName))
            return true;

        if(Category.create(super.getAuthenticatedUser(request), bodyName) != null)
            throw new RequestProcessingException(response.set(CATEGORY_EXISTS_RESPONSE));

        return category.setName(bodyName);
    }
}
