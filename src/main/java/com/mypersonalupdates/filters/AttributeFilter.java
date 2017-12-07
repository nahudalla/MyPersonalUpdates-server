package com.mypersonalupdates.filters;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.AttributeFilterActions;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Esta clase representa un filtro abstracto sobre un atributo
 * de una actualización. {@link com.mypersonalupdates.Update}
 * {@link Filter}
 */
public abstract class AttributeFilter extends Filter {
    public static final String DATABASE_TYPE = "AttributeFilter";

    protected final UpdatesProviderAttribute attr;
    protected final String value;

    protected AttributeFilter(Long ID, UpdatesProviderAttribute attr, String value) {
        super(ID);
        this.attr = attr;
        this.value = value;
    }

    protected static UpdatesProviderAttribute getAttributeFromID(Long id) throws DBException {
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

    protected static String getValueFromID(Long id) throws DBException {
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

    private static String getTypeFromID(Long id) throws DBException {
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

    public static AttributeFilter create(Long ID) throws DBException {
        String type = AttributeFilter.getTypeFromID(ID);

        if(type == null)
            return null;

        if (type.equals(ExactAttributeFilter.DATABASE_TYPE))
            return ExactAttributeFilter.create(ID);

        if (type.equals(PartialAttributeFilter.DATABASE_TYPE))
            return PartialAttributeFilter.create(ID);

        throw new AssertionError();
    }

    protected static Long create(UpdatesProviderAttribute attr, String value, String type) throws DBException {
        Long filterID;

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
                    Long finalFilterID = filterID;
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
    public Collection<String> getValues(UpdatesProviderAttribute attr) {
        if(this.attr.equals(attr))
            return Lists.newArrayList(this.value);
        return null;
    }

    @Override
    protected JsonObject toJSON(String type) {
        JsonObject object = super.toJSON(type);
        object.add("attr", this.attr.toJSON());
        object.addProperty("value", this.value);
        return object;
    }

    protected void injectSQLConditions(LogSQLQuery query, String valueCondition) throws SealedException {
        query.appendToCondition("#ATTRS_TABLE#.attrID = ? AND #ATTRS_TABLE#.providerID = ? AND "+valueCondition);
        query.addLongParam(this.attr.getAttrID());
        query.addLongParam(this.attr.getProvider().getID());
        query.addStringParam(this.value+'*');
    }
}
