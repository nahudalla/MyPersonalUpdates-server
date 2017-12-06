package com.mypersonalupdates.webserver.handlers.category;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;

/**
 * Esta clase se encarga de procesar parte de una petición
 * que necesita de una categoría existente. En particular
 * se encarga de chequear que la categoría exista.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public abstract class CategoryValidator<T> extends UserAuthHandler<T> {
    protected Category getCategory(Request request) {
        return (Category) request.getData(Category.class);
    }

    @Override
    protected void processRequest(Request request, Response response) throws DBException, RequestProcessingException, SealedException {
        super.processRequest(request, response);

        String pathName = request.getPathParam("categoryName");
        Category category;

        if(     pathName == null ||
                null == (category = Category.create(super.getAuthenticatedUser(request), pathName))
                )
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        request.setData(category);
    }
}
