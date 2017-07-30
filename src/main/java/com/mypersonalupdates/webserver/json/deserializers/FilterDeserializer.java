package com.mypersonalupdates.webserver.json.deserializers;

import com.google.gson.*;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.filters.*;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.lang.reflect.Type;

/**
 * Esta clase representa un deserealizador de un filtro,
 * es decir, a partir de su representación en formato
 * JSON, crea una instancia de la clase Filter.
 */
public final class FilterDeserializer implements JsonDeserializer<Filter> {
    @Override
    public Filter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return this.createFilter(json.getAsJsonObject(), context);
        } catch (DBException e) {
            throw new JsonParseException("DBException", e);
        }
    }

    private Filter createFilter(JsonElement json, JsonDeserializationContext context) throws JsonParseException, DBException {
        if(json == null || !json.isJsonObject())
            throw new JsonParseException("Se requiere un objeto.");

        JsonObject object = json.getAsJsonObject();

        JsonElement type = object.get("type");

        if(type == null || !type.isJsonPrimitive() || !type.getAsJsonPrimitive().isString())
            throw new JsonParseException("Se requiere el campo 'type'.");

        switch (type.getAsString()) {
            case NotFilter.DATABASE_TYPE: return this.createNot(object, context);
            case AndFilter.DATABASE_TYPE: return this.createAnd(object, context);
            case OrFilter.DATABASE_TYPE: return this.createOr(object, context);
            case PartialAttributeFilter.DATABASE_TYPE: return this.createPartial(object, context);
            case ExactAttributeFilter.DATABASE_TYPE: return this.createExact(object, context);
        }

        throw new JsonParseException("Campo 'type' inválido.");
    }

    private Filter createExact(JsonObject json, JsonDeserializationContext context) throws JsonParseException, DBException {
        JsonElement value = json.get("value");
        JsonElement attr = json.get("attr");

        if(attr == null || value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString())
            throw new JsonParseException("Campos inválidos.");

        return ExactAttributeFilter.create(
                context.deserialize(attr, UpdatesProviderAttribute.class),
                value.getAsString()
        );
    }

    private Filter createPartial(JsonObject json, JsonDeserializationContext context) throws JsonParseException, DBException {
        JsonElement value = json.get("value");
        JsonElement attr = json.get("attr");

        if(attr == null || value == null || !value.isJsonPrimitive() || ! value.getAsJsonPrimitive().isString())
            throw new JsonParseException("Campos inválidos.");

        return PartialAttributeFilter.create(
                context.deserialize(attr, UpdatesProviderAttribute.class),
                value.getAsString()
        );
    }

    private Filter createOr(JsonObject json, JsonDeserializationContext context) throws JsonParseException, DBException {
        return OrFilter.create(
                this.createFilter(json.get("filter1"), context),
                this.createFilter(json.get("filter2"), context)
        );
    }

    private Filter createAnd(JsonObject json, JsonDeserializationContext context) throws JsonParseException, DBException {
        return AndFilter.create(
                this.createFilter(json.get("filter1"), context),
                this.createFilter(json.get("filter2"), context)
        );
    }

    private Filter createNot(JsonObject json, JsonDeserializationContext context) throws JsonParseException, DBException {
        return NotFilter.create(
                this.createFilter(json.get("filter"), context)
        );
    }
}
