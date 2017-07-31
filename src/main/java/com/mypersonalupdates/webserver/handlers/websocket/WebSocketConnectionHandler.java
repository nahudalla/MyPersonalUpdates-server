package com.mypersonalupdates.webserver.handlers.websocket;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.UserAuthHandler;
import com.mypersonalupdates.webserver.websockets.WebSocketConnectionHandledCallback;

import javax.websocket.Session;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Esta clase se encarga de procesar una petición que solicita
 * conectarse al servicio de actualizaciones en tiempo real
 * para recibir actualizaciones de una categoría específica.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class WebSocketConnectionHandler<T> extends UserAuthHandler<T> {
    @Override
    protected void processRequest(Request request, Response response)
            throws SealedException, RequestProcessingException, DBException {
        super.processRequest(request, response);

        Session session = (Session) request.getData(Session.class);
        WebSocketConnectionHandledCallback callback =
                (WebSocketConnectionHandledCallback) request.getData(WebSocketConnectionHandledCallback.class);
        User user = super.getAuthenticatedUser(request);
        String catName = request.getPathParam("categoryName");

        if (session == null || callback == null || catName == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        try {
            catName = URLDecoder.decode(catName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RequestProcessingException(response.set(Handler.INTERNAL_ERROR_RESPONSE), e);
        }

        Category category = Category.create(user, catName);

        if (category == null)
            throw new RequestProcessingException(response.set(Handler.NOT_FOUND_RESPONSE));

        response.close();
        callback.done(session, user, category);
    }
}
