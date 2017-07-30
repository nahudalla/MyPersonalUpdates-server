package com.mypersonalupdates.webserver.handlers;

import com.google.gson.JsonElement;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.exceptions.SealedException;
import org.eclipse.jetty.http.HttpStatus;

/**
 * Esta clase abstracta se encarga de procesar una petición.
 * @param <ResponseType> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public abstract class Handler<ResponseType> {
    // Declaración de constantes
    public static final Response DEFAULT_RESPONSE;
    public static final Response BAD_REQUEST_RESPONSE;
    public static final Response INTERNAL_ERROR_RESPONSE;
    public static final Response NOT_AUTHORIZED_RESPONSE;
    public static final Response FORBIDDEN_RESPONSE;
    public static final Response NOT_FOUND_RESPONSE;
    public static final Response NOT_ALLOWED_RESPONSE;
    public static final Response NOT_ACCEPTABLE_RESPONSE;
    public static final Response UNSUPPORTED_MEDIA_RESPONSE;
    public static final Response UNAVAILABLE_RESPONSE;
    public static final Response UNKNOWN_RESPONSE;

    // Inicialización de constantes
    static {
        DEFAULT_RESPONSE = new Response();
        BAD_REQUEST_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.BAD_REQUEST)
                .setMessage("Petición no valida.")
                .build();
        INTERNAL_ERROR_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.INTERNAL_SERVER_ERROR)
                .setMessage("Error interno del servidor.")
                .build();
        NOT_AUTHORIZED_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.UNAUTHORIZED)
                .setMessage("No autorizado, se requiere autenticación.")
                .build();
        FORBIDDEN_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.FORBIDDEN)
                .setMessage("Acceso prohibido, se requiere autenticación válida.")
                .build();
        NOT_FOUND_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.NOT_FOUND)
                .setMessage("No se ha podido encontrar el recurso al que intenta acceder.")
                .build();
        NOT_ALLOWED_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.METHOD_NOT_ALLOWED)
                .setMessage("Método no permitido.")
                .build();
        NOT_ACCEPTABLE_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.NOT_ACCEPTABLE)
                .setMessage("Método no permitido.")
                .build();
        UNSUPPORTED_MEDIA_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.UNSUPPORTED_MEDIA_TYPE)
                .setMessage("Formato no soportado.")
                .build();
        UNAVAILABLE_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.SERVICE_UNAVAILABLE)
                .setMessage("Servicio no disponible.")
                .build();
        UNKNOWN_RESPONSE = new Response.Builder()
                .setStatus(HttpStatus.Code.INTERNAL_SERVER_ERROR)
                .setMessage("Error desconocido.")
                .build();
    }

    private ResponseProcessor<ResponseType> responseProcessor;

    protected void setResponseProcessor(ResponseProcessor<ResponseType> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    protected Handler() {
        this.setResponseProcessor(null);
    }

    public Handler(ResponseProcessor<ResponseType> responseProcessor) {
        this.setResponseProcessor(responseProcessor);
    }

    protected abstract void processRequest(Request request, Response response) throws DBException, RequestProcessingException, SealedException;

    public final ResponseType process(Request request) throws RequestProcessingException {
        if(this.responseProcessor == null)
            throw new RequestProcessingException(
                    Handler.INTERNAL_ERROR_RESPONSE,
                    new IllegalStateException("No se ha especificado un procesador de respuesta.")
            );

        try {
            Response response = new Response();

            this.responseProcessor.processBefore(response);

            if(request == null) {
                response.set(Handler.BAD_REQUEST_RESPONSE).close();
            } else {
                this.processRequest(request, response);

                if (!request.canBeEmpty() && request.isEmpty())
                    response.set(Handler.BAD_REQUEST_RESPONSE).close();
                else if (!response.isClosed() && response.isEmpty())
                    response.set(Handler.DEFAULT_RESPONSE).close();

                JsonElement echo = request.getBodyItem("echo");

                if (echo != null)
                    response.setEcho(echo);
            }

            return this.responseProcessor.processAfter(response);
        }catch (SealedException | DBException e) {
            throw new RequestProcessingException(Handler.INTERNAL_ERROR_RESPONSE, e);
        }
    }

    public static class Builder<T> {
        private final ResponseProcessor<T> responseProcessor;

        public Builder(ResponseProcessor<T> responseProcessor) {
            this.responseProcessor = responseProcessor;
        }

        public <T2 extends Handler<T>> T2 build(T2 handler) {
            handler.setResponseProcessor(this.responseProcessor);
            return handler;
        }
    }
}
