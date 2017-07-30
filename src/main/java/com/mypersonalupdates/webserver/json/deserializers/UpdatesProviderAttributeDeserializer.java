package com.mypersonalupdates.webserver.json.deserializers;

import com.google.gson.*;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.lang.reflect.Type;

/**
 * Esta clase representa un deserealizador de un atributo de
 * un proveedor, es decir, a partir de su representación en
 * formato JSON, crea una instancia de la clase
 * UpdatesProviderAttribute.
 */
public final class UpdatesProviderAttributeDeserializer implements JsonDeserializer<UpdatesProviderAttribute>{
    @Override
    public UpdatesProviderAttribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json == null || !json.isJsonObject())
            throw new JsonParseException("Se requiere un objeto.");

        JsonObject object = json.getAsJsonObject();

        JsonElement attrID = object.get("attrID");
        JsonElement providerID = object.get("providerID");

        if(attrID == null || providerID == null ||
                !attrID.isJsonPrimitive() || !attrID.getAsJsonPrimitive().isNumber() ||
                !providerID.isJsonPrimitive() || !attrID.getAsJsonPrimitive().isNumber())
            throw new JsonParseException("Campos inválidos.");

        UpdatesProvider provider = UpdatesProvidersManager.getInstance().getProvider(providerID.getAsLong());

        if(provider == null)
            throw new JsonParseException("El proveedor no existe.");

        UpdatesProviderAttribute attribute;
        try {
            attribute =  UpdatesProviderAttribute.create(
                    provider,
                    attrID.getAsLong()
            );
        } catch (DBException e) {
            throw new JsonParseException("DBException", e);
        }

        if (attribute == null)
            throw new JsonParseException("El atributo no existe.");

        return attribute;
    }
}
