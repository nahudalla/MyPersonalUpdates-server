package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.AttributeFilterActions;
import com.mypersonalupdates.db.actions.UpdatesProviderAttributeActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public abstract class AttributeFilter extends Filter{

    private Integer ID;
    protected UpdatesProviderAttribute attr;
    protected String value;

    protected AttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        this.ID = ID;
        this.attr = attr;
        this.value = value;
    }

    //TODO: Agregar al diagrama de clases
    protected static Integer create(UpdatesProviderAttribute attr, String value, String type) throws DBException {
        Integer attrID;

        try {
            attrID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdatesProviderAttributeActions.class).getIDFromContent(
                            attr.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (attrID != null) {
            attrID = Filter.create("AttributeFilter");

            int rowsAffected = 0;
            final Integer aID = attrID;

            if (attrID != null){
                try {
                    rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(AttributeFilterActions.class).create(
                                    aID,
                                    attr.getID(),
                                    value,
                                    type
                            )
                    );
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }

            if (rowsAffected <= 0){
                try {
                    DBConnection.getInstance().withHandle(
                            handle -> handle.attach(AttributeFilterActions.class).remove(
                                    aID
                            )
                    );
                } catch (Exception e) {
                    throw new DBException(e);
                }

                attrID = null;
            }
        }

        return attrID;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider) {
        if (this.attr.getProvider().equals(provider)) {
            Collection<UpdatesProviderAttribute> attributes = new LinkedList<>();
            attributes.add(this.attr);
            return attributes;
        }

        return null;
    }

    @Override
    public Integer getID() {
        return this.ID;
    }
}
