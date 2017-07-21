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
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getAttrFromID(
                            id
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar al diagrama de clases
    protected static String getValueFromID(Integer id) throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getFieldValueFromID(
                            id
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar al diagrama de clases
    private static String getTypeFromID(Integer id) throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getTypeFromID(
                            id
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar al diagrama de clases
    public static AttributeFilter create(Integer ID) throws DBException {
        String type = AttributeFilter.getTypeFromID(ID);

        // TODO: Implementar en UpdatesProviderAttribute create

        if(type == null)
            return null;

        if (type.equals(ExactAttributeFilter.DATABASE_TYPE))
            return ExactAttributeFilter.create(ID);

        if (type.equals(PartialAttributeFilter.DATABASE_TYPE))
            return PartialAttributeFilter.create(ID);

        return null;
    }

    //TODO: Agregar al diagrama de clases
    protected static Integer create(UpdatesProviderAttribute attr, String value, String type) throws DBException {
        Integer filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(AttributeFilterActions.class).getIDFromContent(
                            attr.getProvider().getID(),
                            attr.getAttrID(),
                            value,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterID == null) {
            filterID = Filter.create(AttributeFilter.DATABASE_TYPE);

            if (filterID != null){
                try {
                    Integer finalFilterID = filterID;
                    int rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(AttributeFilterActions.class).create(
                                    finalFilterID,
                                    attr.getProvider().getID(),
                                    attr.getAttrID(),
                                    value,
                                    type
                            )
                    );
                    if(rowsAffected <= 0) {
                        Filter.removeFilterByID(filterID);
                        filterID = null;
                    }
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }
        }
        return filterID;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes(UpdatesProvider provider) {
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
