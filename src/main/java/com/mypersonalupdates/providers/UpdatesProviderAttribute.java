package com.mypersonalupdates.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdatesProviderAttributeActions;
import com.mypersonalupdates.webserver.json.JSONSerializable;

/**
 * Esta clase representa un atributo que puede tener una
 * actualización {@link com.mypersonalupdates.Update} de
 * un proveedor {@link UpdatesProvider}.
 */
public final class UpdatesProviderAttribute implements JSONSerializable {
    private final Long attrID;
    private final UpdatesProvider provider;
    private final Boolean multi;

    private UpdatesProviderAttribute(UpdatesProvider provider, Long attrID, Boolean multi) {
        this.attrID = attrID;
        this.provider = provider;
        this.multi = multi;
    }

    public static UpdatesProviderAttribute create(UpdatesProvider provider, Long attrID) throws DBException {
        Boolean multi;

        try {
            multi = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdatesProviderAttributeActions.class).getMulti(
                            attrID,
                            provider.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return (multi == null) ? null : new UpdatesProviderAttribute(provider, attrID, multi);
    }

    public Long getAttrID() {
        return this.attrID;
    }

    public UpdatesProvider getProvider() {
        return this.provider;
    }

    public String getName() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdatesProviderAttributeActions.class).getName(
                            this.attrID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public String getDescription() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdatesProviderAttributeActions.class).getDescription(
                            this.attrID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public boolean isMultivalued() {
        return this.multi;
    }

    public String getFilterNotes() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdatesProviderAttributeActions.class).getFilterNotes(
                            this.attrID,
                            this.provider.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (!(o instanceof UpdatesProviderAttribute))
            return false;

        UpdatesProviderAttribute attribute = (UpdatesProviderAttribute) o;

        return this.getProvider().getID().equals(attribute.getProvider().getID()) &&
                this.getAttrID().equals(attribute.getAttrID());
    }

    @Override
    public JsonElement toJSON() {
        JsonObject object = new JsonObject();
        object.addProperty("attrID", this.attrID);
        object.addProperty("providerID", this.provider.getID());
        return object;
    }
}
