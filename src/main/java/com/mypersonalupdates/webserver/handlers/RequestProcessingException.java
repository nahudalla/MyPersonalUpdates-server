package com.mypersonalupdates.webserver.handlers;

import com.mypersonalupdates.webserver.Response;

/**
 * Esta clase es una excepción que representa que
 * hubo un error al procesar una petición. Este
 * error puede deberse bien a un error interno del
 * servidor o a que los datos recibidos con la
 * peticion no son correctos.
 */
public class RequestProcessingException extends Exception {
    private final Response response;

    public RequestProcessingException(Response response) {
        super(response.toString());
        this.response = response;
    }

    public RequestProcessingException(Response response, Throwable cause) {
        super(response.toString(), cause);
        this.response = response;
    }


    protected RequestProcessingException(Response response, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(response.toString(), cause, enableSuppression, writableStackTrace);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
