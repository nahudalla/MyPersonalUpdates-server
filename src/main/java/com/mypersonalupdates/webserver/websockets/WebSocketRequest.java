package com.mypersonalupdates.webserver.websockets;

import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import javax.websocket.Session;
import java.util.*;

/**
 * Esta clase representa una petición recibida por el
 * servicio de streaming en tiempo real.
 */
public final class WebSocketRequest extends Request {
    protected WebSocketRequest(Map<String, ? extends Collection<String>> pathParams, Map<String, List<String>> queryParams)
            throws RequestProcessingException {
        super(pathParams, queryParams, null, null);
    }

    public static WebSocketRequest create(
            Session session,
            String accessToken,
            String categoryName,
            WebSocketConnectionHandledCallback callback
    ) throws RequestProcessingException {
        Map<String, List<String>> pathParams = new Hashtable<>();
        Map<String, List<String>> queryParams = new Hashtable<>();

        List<String> list = new LinkedList<>();

        queryParams.put("token", list);
        list.add(accessToken);

        list = new LinkedList<>();

        pathParams.put("categoryName", list);
        list.add(categoryName);

        WebSocketRequest request = new WebSocketRequest(pathParams, queryParams);

        request.setData(session, Session.class);
        request.setData(callback, WebSocketConnectionHandledCallback.class);

        return request;
    }
}
