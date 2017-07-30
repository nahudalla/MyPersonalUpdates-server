package com.mypersonalupdates.webserver;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

/**
 * Esta clase representa los datos de una conexión
 * con el servidor.
 */
public abstract class ConnectionData {
    protected static final Gson JSON = Server.JSON;

    private Map<String, Collection<String>> headers = null;
    private JsonObject body = null;
    private boolean isClosed = false;

    protected void writeAttempt() throws SealedException {
        if (this.isClosed)
            throw new SealedException("No se puede modificar una respuesta que ya ha sido cerrada.");
    }

    protected ConnectionData() {}

    protected ConnectionData(
            Map<String, ? extends Collection<String>> headers,
            String JSONBody
    ) throws RequestProcessingException {
        try {
            JsonElement data = JSONBody == null ? null : JSON.fromJson(JSONBody, JsonElement.class);
            this.body = data == null ? null : data.getAsJsonObject();
        }catch (Exception e) {
            throw new RequestProcessingException(Handler.BAD_REQUEST_RESPONSE, e);
        }

        this.headers = headers == null ? null : new Hashtable<>(headers);
    }

    /* Observadores */

    public boolean hasHeaders() {
        return this.headers != null && !this.headers.isEmpty();
    }

    public boolean hasBody() {
        return this.body != null && this.body.size() > 0;
    }


    public boolean isClosed() {
        return this.isClosed;
    }

    public boolean isEmpty() {
        // Los headers no participan para saber si una petición esta vacía
        return !this.hasBody();
    }

    /* Getters */

    protected Map<String, Collection<String>> getHeaders() {
        return this.headers;
    }

    protected Collection<String> getHeader(String name) {
        return this.headers == null ? null : this.headers.get(name);
    }

    protected JsonObject getBody() {
        return this.body;
    }

    protected JsonElement getBodyItem(String name) {
        return this.body == null ? null : this.body.get(name);
    }

    protected <T> T getBodyItem(String name, Class<T> tClass) throws RequestProcessingException {
        JsonElement element = this.getBodyItem(name);

        try {
            return element == null ? null : JSON.fromJson(element, tClass);
        }catch (Exception e) {
            throw new RequestProcessingException(Handler.BAD_REQUEST_RESPONSE, e);
        }
    }

    protected <T> T getBodyItem(String name, Type type) throws RequestProcessingException {
        JsonElement element = this.getBodyItem(name);

        try {
            return element == null ? null : JSON.fromJson(element, type);
        }catch (Exception e) {
            throw new RequestProcessingException(Handler.BAD_REQUEST_RESPONSE, e);
        }
    }

    /* Setters */

    protected ConnectionData set(ConnectionData other) throws SealedException {
        this.writeAttempt();

        this.copyFrom(other);
        return this;
    }

    protected ConnectionData set(JsonObject jsonObject) throws SealedException {
        this.writeAttempt();

        this.body = jsonObject;
        return this;
    }

    protected ConnectionData set(String name, JsonElement jsonElement) throws SealedException {
        this.writeAttempt();

        if (this.body == null)
            this.body = new JsonObject();

        this.body.add(name, jsonElement);

        return this;
    }

    protected ConnectionData setHeader(String name, String value) throws SealedException {
        this.writeAttempt();

        if(this.headers == null)
            this.headers = new Hashtable<>();

        Collection<String> header = this.headers.computeIfAbsent(name, k -> new LinkedList<>());

        header.add(value);

        return this;
    }

    /* Modificadores */

    protected ConnectionData clear()  throws SealedException {
        this.writeAttempt();

        this.headers = null;
        this.body = null;

        return this;
    }

    protected ConnectionData merge(ConnectionData other) throws SealedException {
        this.writeAttempt();

        if(other.headers != null)
            this.headers.putAll(other.headers);

        if (this.body == null)
            this.body = other.body.deepCopy();
        else {
            for (Map.Entry<String, JsonElement> entry : other.body.entrySet()) {
                this.body.add(entry.getKey(), entry.getValue().deepCopy());
            }
        }

        return this;
    }

    protected ConnectionData merge(JsonObject data) throws SealedException {
        this.writeAttempt();

        if (this.body == null)
            this.body = data;
        else {
            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                this.body.add(entry.getKey(), entry.getValue());
            }
        }

        return this;
    }

    protected ConnectionData close() {
        this.isClosed = true;

        return this;
    }

    /* Otros */
    protected void copyFrom(ConnectionData other) throws SealedException {
        this.writeAttempt();

        this.headers = other.headers == null ? null : new Hashtable<>(other.headers);
        this.body = other.body == null ? null : other.body.deepCopy();
    }
}
