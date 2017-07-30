package com.mypersonalupdates.webserver.responses.builders;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.responses.BuilderBase;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase UpdatesProviderAttribute.
 */
public final class ProviderAttributeResponseBuilder extends BuilderBase<ProviderAttributeResponseBuilder> {
    private final UpdatesProviderAttribute attribute;

    public ProviderAttributeResponseBuilder(UpdatesProviderAttribute attribute) {
        super("ProviderAttribute");
        this.attribute = attribute;
    }

    public ProviderAttributeResponseBuilder includeID() {
        this.jsonObject.addProperty("attrID", this.attribute.getAttrID());
        return this;
    }

    public ProviderAttributeResponseBuilder includeProviderID() {
        this.jsonObject.addProperty("providerID", this.attribute.getProvider().getID());
        return this;
    }

    public ProviderAttributeResponseBuilder includeName() throws DBException {
        this.jsonObject.addProperty("name", this.attribute.getName());
        return this;
    }

    public ProviderAttributeResponseBuilder includeDescription() throws DBException {
        this.jsonObject.addProperty("description", this.attribute.getDescription());
        return this;
    }

    public ProviderAttributeResponseBuilder includeFilterNotes() throws DBException {
        this.jsonObject.addProperty("filterNotes", this.attribute.getFilterNotes());
        return this;
    }

    public ProviderAttributeResponseBuilder includeMultivalued() {
        this.jsonObject.addProperty("multivalued", this.attribute.isMultivalued());
        return this;
    }
}
