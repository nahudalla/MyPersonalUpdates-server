package com.mypersonalupdates.webserver.responses;

import java.lang.reflect.Type;

/**
 * Esta clase almacena los datos parciales de una
 * respuesta a una petición.
 * */
public final class ResponseData {
    private final Object object;
    private Type type = null;

    public ResponseData(Object object) {
        this.object = object;
    }

    public ResponseData(Object object, Type objectType) {
        this.object = object;
        this.type = objectType;
    }

    public Object getObject() {
        return object;
    }

    public Type getType() {
        return type;
    }
}
