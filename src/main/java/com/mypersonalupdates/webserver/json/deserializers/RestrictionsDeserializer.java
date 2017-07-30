package com.mypersonalupdates.webserver.json.deserializers;

import com.google.gson.*;
import com.mypersonalupdates.log.Restrictions;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Esta clase representa un deserealizador de una restricción
 * de búsqueda, es decir, a partir de su representación en
 * formato JSON, crea una instancia de la clase Restrictions.
 */
public final class RestrictionsDeserializer implements JsonDeserializer<Restrictions> {
    @Override
    public Restrictions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json == null) return null;

        if(!json.isJsonObject())
            throw new JsonParseException("'restrictions' no es un objeto.");

        JsonObject object = json.getAsJsonObject();

        Long fromID = this.deserializeLong(object.get("fromID"));
        Long limit = this.deserializeLong(object.get("limit"));
        Instant startTimestamp = this.deserializeTimestamp(object.get("startTimestamp"), context);
        Instant endTimestamp = this.deserializeTimestamp(object.get("endTimestamp"), context);
        Boolean order = this.deserializeOrder(object.get("order"));

        return new Restrictions(fromID, startTimestamp, endTimestamp, order, limit);
    }

    private Boolean deserializeOrder(JsonElement element) {
        if(element == null) return null;

        if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean())
            throw new JsonParseException("'restrictions.order' no es un booleano.");

        return element.getAsBoolean();
    }

    private Instant deserializeTimestamp(JsonElement element, JsonDeserializationContext context) {
        if(element == null) return null;

        return context.deserialize(element, Instant.class);
    }

    private Long deserializeLong(JsonElement element) {
        if(element == null) return null;

        if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("'restrictions.fromID' o 'restrictions.limit' no son/es un número.");

        return element.getAsLong();
    }
}
