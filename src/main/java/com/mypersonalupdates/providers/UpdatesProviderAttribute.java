package com.mypersonalupdates.providers;

import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdatesProviderAttributeActions;

public class UpdatesProviderAttribute {

    private final Integer attrID;
    private final UpdatesProvider provider;
    private final Boolean multi;

    private UpdatesProviderAttribute(UpdatesProvider provider, Integer attrID, Boolean multi) {
        this.attrID = attrID;
        this.provider = provider;
        this.multi = multi;
    }

    public static UpdatesProviderAttribute create(UpdatesProvider provider, Integer attrID) throws DBException {
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

    public Integer getAttrID() {
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
}
