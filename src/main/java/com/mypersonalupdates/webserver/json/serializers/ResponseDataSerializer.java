package com.mypersonalupdates.webserver.json.serializers;

import com.google.gson.*;
import com.mypersonalupdates.webserver.responses.ResponseData;

import java.lang.reflect.Type;

/**
 * Esta clase representa un serializador de los datos de
 * respuesta a una petición, es decir, a partir de un
 * objeto y opcionalmente su tipo, genera su representación
 * en formato JSON.
 */
public final class ResponseDataSerializer implements JsonSerializer<ResponseData> {
    @Override
    public JsonElement serialize(ResponseData src, Type typeOfSrc, JsonSerializationContext context) {
        if(src == null)
            return null;

        JsonElement jsonElement;

        if(src.getType() == null)
            jsonElement = context.serialize(src.getObject());
        else
            jsonElement = context.serialize(src.getObject(), src.getType());

        return jsonElement;
    }
}
