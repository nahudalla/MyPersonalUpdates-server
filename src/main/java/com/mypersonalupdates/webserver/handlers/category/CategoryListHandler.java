package com.mypersonalupdates.webserver.handlers.category;

import com.google.gson.reflect.TypeToken;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import com.mypersonalupdates.webserver.responses.ResponseData;
import com.mypersonalupdates.webserver.responses.builders.CategoryResponseBuilder;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase se encarga de procesar una petición de
 * listar todas las categorías asociadas a un usuario.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CategoryListHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, Response response) throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Type type = new TypeToken<List<ResponseData>>(){}.getType();
        List<ResponseData> categories = new LinkedList<>();

        for (Category category : super.getAuthenticatedUser(request).getCategories()) {
            categories.add(
                    new CategoryResponseBuilder(category)
                            .includeName()
                            .build()
            );
        }

        response.setType("CategoryList")
                .set("categories", categories, type)
                .close();
    }
}
