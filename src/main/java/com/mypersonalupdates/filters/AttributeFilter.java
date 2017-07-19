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

    private Integer ID;
    protected UpdatesProviderAttribute attr;
    protected String value;

    protected AttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        this.ID = ID;
        this.attr = attr;
        this.value = value;
    }

    //TODO: Agregar al diagrama de clases
    protected static UpdatesProviderAttribute getAttributeFromID(Integer id) throws DBException {
        Integer attrID;

        try {
            attrID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getAttrIDFromID(
                            id
                    )
            );
            return attrID == null ? null : UpdatesProviderAttribute.create(attrID);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar al diagrama de clases
    protected static String getValueFromID(Integer id) throws DBException {
        String fieldValue;

        try {
            fieldValue = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getFieldValueFromID(
                            id
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return fieldValue;
    }

    //TODO: Agregar al diagrama de clases
    private static String getTypeFromID(Integer id) throws DBException {
        String type;

        try {
            type = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getTypeFromID(
                            id
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return type;
    }

    //TODO: Agregar al diagrama de clases
    public static AttributeFilter create(Integer ID) throws DBException {
        String type = AttributeFilter.getTypeFromID(ID);

        // TODO: Implementar en UpdatesProviderAttribute create

        if (type.equals(ExactAttributeFilter.DATABASE_TYPE))
            return ExactAttributeFilter.create(ID);

        if (type.equals(PartialAttributeFilter.DATABASE_TYPE))
            return PartialAttributeFilter.create(ID);

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

        if (attrID == null) {
            attrID = Filter.create(DATABASE_TYPE);

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

            if(rowsAffected <= 0) {
                Filter.removeFilterByID(attrID);
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
