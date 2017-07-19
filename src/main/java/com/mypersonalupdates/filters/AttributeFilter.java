package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.AttributeFilterActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public abstract class AttributeFilter extends Filter{

    public static final String DATABASE_TYPE = "AttributeFilter";
    static final String exact = "ExactAttributeFilter";
    static final String parcial = "ParcialAttributeFilter";

    private Integer ID;
    protected UpdatesProviderAttribute attr;
    protected String value;

    protected AttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        this.ID = ID;
        this.attr = attr;
        this.value = value;
    }

    //TODO: Agregar al diagrama de clases
    public static AttributeFilter create(Integer ID) throws DBException {
        Integer attrID;
        String fieldValue, type;

        try {
            type = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getTypeFromID(
                            ID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }


        try {
            attrID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getAttrIDFromKeys(
                            ID,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        try {
            fieldValue = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getFieldValueDFromKeys(
                            ID,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        // TODO: Implementar en UpdatesProviderAttribute create

        if (type.equals(exact))
            return ExactAttributeFilter.create(ID, UpdatesProviderAttribute.create(attrID), fieldValue);

        if (type.equals(parcial))
            return PartialAttributeFilter.create(ID, UpdatesProviderAttribute.create(attrID), fieldValue);

        return null;
    }

    //TODO: Agregar al diagrama de clases
    protected static Integer create(UpdatesProviderAttribute attr, String value, String type) throws DBException {
        Integer attrID;

        try {
            attrID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getIDFromContent(
                            attr.getID(),
                            value,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        boolean okCreate = true;

        if (attrID == null) {
            attrID = Filter.create(DATABASE_TYPE);

            int rowsAffected;
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

                if(rowsAffected <= 0) {
                    Filter.removeFilterByID(attrID);
                    attrID = null;
                }
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
