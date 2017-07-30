package com.mypersonalupdates.webserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.webserver.responses.*;
import com.mypersonalupdates.exceptions.SealedException;
import org.eclipse.jetty.http.HttpStatus;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Esta clase repsenta la respuesta generada al procesar
 * una petición recibida en el sistema y almacena su
 * información.
 */
public final class Response extends ConnectionData{
    private static final HttpStatus.Code DEFAULT_STATUS_CODE = HttpStatus.Code.OK;

    private HttpStatus.Code status = Response.DEFAULT_STATUS_CODE;

    private boolean includeStatusInBody = true;

    @Override
    public Response clear() throws SealedException {
        super.clear();

        this.status = Response.DEFAULT_STATUS_CODE;

        return this;
    }

    private void copyFrom(Response other) {
        if(other == null) {
            try {
                this.clear();
                return;
            } catch (SealedException e) {
                throw new AssertionError(e);
            }
        }

        try {
            super.copyFrom(other);
        } catch (SealedException e) {
            throw new AssertionError(e);
        }

        this.status = other.status;
        this.includeStatusInBody = other.includeStatusInBody;
    }

    public Response() {}

    public Response(Response other) {
        this.copyFrom(other);
    }

    /* Body */

    public Response set(Response other) throws SealedException {
        this.writeAttempt();

        this.copyFrom(other);
        return this;
    }

    @Override
    public Response set(JsonObject jsonObject) throws SealedException {
        return (Response) super.set(jsonObject);
    }

    @Override
    public Response set(String name, JsonElement jsonElement) throws SealedException {
        return (Response) super.set(name, jsonElement);
    }

    public Response set(ResponseData data) throws SealedException, IllegalArgumentException {
        this.writeAttempt();

        JsonElement element = JSON.toJsonTree(data);

        if(!element.isJsonObject())
            throw new IllegalArgumentException("El cuerpo de la respuesta sólo puede ser un objeto JSON.");

        return this.set(element.getAsJsonObject());
    }

    public Response set(String name, ResponseData data) throws SealedException {
        return this.set(name, JSON.toJsonTree(data));
    }

    public Response set(String name, Object o) throws SealedException {
        return this.set(name, new ResponseData(o));
    }

    public Response set(String name, Object o, Type t) throws SealedException {
        return this.set(name, new ResponseData(o, t));
    }

    public Response setMessage(String message) throws SealedException {
        return this.set("message", message);
    }

    public Response setType(String type) throws SealedException {
        return this.set("type", type);
    }

    public Response merge(Response other) throws SealedException {
        this.writeAttempt();

        this.status = other.status;
        this.includeStatusInBody = other.includeStatusInBody;

        return (Response) super.merge(other);
    }

    public Response setEcho(JsonElement echo) {
        if(this.getBody() != null)
            this.getBody().add("echo", echo);
        return this;
    }

    @Override
    public Response merge(JsonObject data) throws SealedException {
        return (Response) super.merge(data);
    }

    /* Headers */

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return super.getHeaders();
    }

    @Override
    public Collection<String> getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public Response setHeader(String name, String value) throws SealedException {
        return (Response) super.setHeader(name, value);
    }

    /* Others */

    @Override
    public Response close() {
        if (!this.isClosed()) {
            if (this.getBody() != null && this.includeStatusInBody)
                this.getBody().addProperty("status", this.status.getCode());
        }

        return (Response) super.close();
    }

    public boolean getIncludeStatusInBody() {
        return this.includeStatusInBody;
    }

    public Response setIncludeStatusInBody(boolean includeStatusInBody) throws SealedException {
        this.writeAttempt();

        this.includeStatusInBody = includeStatusInBody;
        return this;
    }

    public Response setStatus(HttpStatus.Code status) throws SealedException {
        this.writeAttempt();

        this.status = status;
        return this;
    }

    public HttpStatus.Code getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        this.close();

        return this.getBody() == null ? "" : this.getBody().toString();
    }

    public static final class Builder {
        private final Response response;

        public Builder(Response other) {
            this.response = new Response(other);
        }

        public Builder() {
            this.response = new Response();
        }

        public Builder setStatus(HttpStatus.Code status) {
            try {
                this.response.setStatus(status);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }

            return this;
        }

        public Builder set(JsonObject data) {
            try {
                this.response.set(data);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }
            return this;
        }

        public Builder set(ResponseData data) throws IllegalArgumentException {
            try {
                this.response.set(data);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }
            return this;
        }

        public Builder setMessage(String message) {
            try {
                this.response.setMessage(message);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }

            return this;
        }

        public Builder setType(String message) {
            try {
                this.response.setType(message);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }

            return this;
        }

        public Builder setIncludeStatusInBody(boolean includeStatusInBody) {
            try {
                this.response.setIncludeStatusInBody(includeStatusInBody);
            } catch (SealedException e) {
                throw new AssertionError(e);
            }
            return this;
        }

        public Response build() {
            return this.response;
        }
    }
}
