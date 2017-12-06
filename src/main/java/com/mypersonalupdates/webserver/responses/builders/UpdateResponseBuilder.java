package com.mypersonalupdates.webserver.responses.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.responses.BuilderBase;

import java.util.Collection;
import java.util.Iterator;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase Update.
 */
public final class UpdateResponseBuilder extends BuilderBase<UpdateResponseBuilder> {
    private final Update update;

    public UpdateResponseBuilder(Update update) {
        super("Update");
        this.update = update;
    }

    public UpdateResponseBuilder includeID() {
        Long id = this.update.getID();
        if(id != null)
            this.jsonObject.addProperty("id", id);
        return this;
    }

    public UpdateResponseBuilder includeIDFromProvider() {
        this.jsonObject.addProperty("IDFromProvider", this.update.getIDFromProvider());
        return this;
    }

    public UpdateResponseBuilder includeProviderID() {
        this.jsonObject.addProperty("providerID", this.update.getProvider().getID());
        return this;
    }

    public UpdateResponseBuilder includeTimestamp() {
        this.jsonObject.addProperty("timestamp", this.update.getTimestamp().toEpochMilli());
        return this;
    }

    public UpdateResponseBuilder includeAttributes() throws DBException {
        JsonObject object = new JsonObject();

        for (UpdatesProviderAttribute attribute : this.update.getProvider().getAttributes()) {
            Collection<String> values = this.update.getAttributeValues(attribute);
            Iterator<String> it = values.iterator();

            if(!it.hasNext()) continue;

            if(!attribute.isMultivalued())
                object.addProperty(attribute.getName(), it.next());
            else {
                JsonArray array = new JsonArray(values.size());

                while (it.hasNext()) array.add(it.next());

                object.add(attribute.getName(), array);
            }
        }

        this.jsonObject.add("attributes", object);

        return this;
    }
}
