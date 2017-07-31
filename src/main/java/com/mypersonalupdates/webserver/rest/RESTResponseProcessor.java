package com.mypersonalupdates.webserver.rest;

import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.ResponseProcessor;

import java.util.Collection;
import java.util.Map;

/**
 * Esta clase representa el procesador de la
 * respuesta interna generada por una petición
 * en la API REST.
 */
public final class RESTResponseProcessor implements ResponseProcessor<javax.ws.rs.core.Response> {
    @Override
    public void processBefore(Response response) throws SealedException {
        response.setIncludeStatusInBody(false);
    }

    @Override
    public javax.ws.rs.core.Response processAfter(Response response) {
        javax.ws.rs.core.Response.ResponseBuilder httpResponse = javax.ws.rs.core.Response
                .status(response.getStatus().getCode());

        if(!response.isEmpty()) {
            httpResponse.entity(response.toString());

            boolean contentTypeSet = false;

            if(response.hasHeaders()) {
                for (Map.Entry<String, Collection<String>> entry : response.getHeaders().entrySet()) {
                    String name = entry.getKey();
                    for (String header : entry.getValue())
                        httpResponse.header(name, header);

                    if (name.equals("Content-Type"))
                        contentTypeSet = true;
                }
            }

            if(!contentTypeSet)
                httpResponse.header("Content-Type", "application/json; charset=UTF-8");
        }

        return httpResponse.build();
    }
}
