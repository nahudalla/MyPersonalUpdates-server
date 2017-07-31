package com.mypersonalupdates.webserver.websockets;

import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.ResponseProcessor;

/**
 * Esta clase representa un procesador que se encarga
 * de transformar la respuesta interna a una petición
 * del servicio de streaming en formato JSON.
 */
public final class WebSocketResponseProcessor implements ResponseProcessor<String> {
    @Override
    public void processBefore(Response response) throws SealedException {
        response.setIncludeStatusInBody(true);
    }

    @Override
    public String processAfter(Response response) {
        return response.toString();
    }
}
