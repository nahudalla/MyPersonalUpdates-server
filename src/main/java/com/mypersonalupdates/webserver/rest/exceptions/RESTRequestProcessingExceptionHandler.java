package com.mypersonalupdates.webserver.rest.exceptions;

import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.ResponseProcessor;
import com.mypersonalupdates.webserver.rest.RESTResponseProcessor;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Esta clase se encarga de interceptar todas las
 * excepciones que pueden ocurrir durante el
 * procesamiento de una petición recibida mediante
 * la API REST.
 */
@Provider
public class RESTRequestProcessingExceptionHandler implements ExceptionMapper<RequestProcessingException> {
    private static final ResponseProcessor<Response> processor = new RESTResponseProcessor();

    @Override
    public Response toResponse(RequestProcessingException e) {
        if(e.getResponse().getStatus().isServerError())
            e.printStackTrace(); // TODO: log

        return processor.processAfter(e.getResponse());
    }
}
