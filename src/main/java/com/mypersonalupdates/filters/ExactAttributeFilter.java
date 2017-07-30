package com.mypersonalupdates.filters;

import com.google.gson.JsonElement;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

/**
 * Esta clase representa un filtro sobre el valor exacto de
 * un atributo de una actualización.
 * {@link com.mypersonalupdates.Update} {@link Filter}
 */
public final class ExactAttributeFilter extends AttributeFilter{
    public static final String DATABASE_TYPE = "ExactAttributeFilter";

    public static ExactAttributeFilter create(Long ID) throws DBException {
        String fieldValue = AttributeFilter.getValueFromID(ID);
        UpdatesProviderAttribute attr = AttributeFilter.getAttributeFromID(ID);

        if(fieldValue != null && attr != null)
            return new ExactAttributeFilter(ID, attr, fieldValue);
        
        return null;
    }

    private ExactAttributeFilter(Long ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public static ExactAttributeFilter create(UpdatesProviderAttribute attr, String value) throws DBException {
        Long filterID;
        filterID = AttributeFilter.create(attr, value, ExactAttributeFilter.DATABASE_TYPE);
        return filterID == null ? null : new ExactAttributeFilter(filterID, attr, value);
    }

    @Override
    public boolean test(Update update) {
        Collection<String> attributeValues = update.getAttributeValues(this.attr);

        return attributeValues == null ? (this.value == null) : attributeValues.contains(this.value);
    }

    @Override
    public JsonElement toJSON() {
        return super.toJSON(ExactAttributeFilter.DATABASE_TYPE);
    }

    @Override
    public void injectSQLConditions(LogSQLQuery query) throws SealedException {
        super.injectSQLConditions(query, "#ATTRS_TABLE#.value = ?");
    }
}
