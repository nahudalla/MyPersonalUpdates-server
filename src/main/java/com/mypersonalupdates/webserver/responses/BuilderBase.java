package com.mypersonalupdates.webserver.responses;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;

/**
 * Esta clase abstracta es la base de todas
 * las clases que se encargan de generar
 * respuestas a las peticiones.
 * @param <T> Tipo de la clase que la extiende para ser devuelto por los métodos
 */
public abstract class BuilderBase<T extends BuilderBase> {
    protected final JsonObject jsonObject;
    private final String type;

    public BuilderBase(String type) {
        this.jsonObject = new JsonObject();
        this.type = type;
    }

    private JsonArray newArray(String name, int size) {
        JsonArray array = new JsonArray(size);
        this.jsonObject.add(name, array);
        return array;
    }

    protected void addJsonCollection(String name, Collection<JsonElement> collection) {
        JsonArray array = this.newArray(name, collection.size());
        for (JsonElement element : collection) array.add(element);
    }

    protected void addBooleanCollection(String name, Collection<Boolean> collection) {
        JsonArray array = this.newArray(name, collection.size());
        for (Boolean element : collection) array.add(element);
    }

    protected void addNumberCollection(String name, Collection<Number> collection) {
        JsonArray array = this.newArray(name, collection.size());
        for (Number element : collection) array.add(element);
    }

    protected void addStringCollection(String name, Collection<String> collection) {
        JsonArray array = this.newArray(name, collection.size());
        for (String element : collection) array.add(element);
    }

    protected void addCharacterCollection(String name, Collection<Character> collection) {
        JsonArray array = this.newArray(name, collection.size());
        for (Character element : collection) array.add(element);
    }

    @SuppressWarnings("unchecked")
    public T includeType() {
        this.jsonObject.addProperty("type", this.type);
        return (T) this;
    }

    public ResponseData build() {
        return new ResponseData(this.jsonObject);
    }

    public JsonObject buildObject() {
        return this.jsonObject;
    }
}
