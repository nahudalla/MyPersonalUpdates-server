package com.mypersonalupdates.webserver.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Esta clase representa un serializador/deserealizador de
 * un timestamp (instante de tiempo), es decir, a partir de
 * su representación en formato JSON, crea una instancia de
 * la clase Instant y vice versa.
 */
public final class InstantSerializerDeserializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json == null) return null;

        if(!json.isJsonPrimitive() || json.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Not an Instant.");

        return Instant.ofEpochMilli(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        if(src == null) return JsonNull.INSTANCE;
        return new JsonPrimitive(src.toEpochMilli());
    }
}
