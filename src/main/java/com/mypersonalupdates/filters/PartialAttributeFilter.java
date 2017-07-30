package com.mypersonalupdates.filters;

import com.google.gson.JsonElement;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

/**
 * Esta clase representa un filtro sobre un valor que está
 * contenido de manera parcial dentro de un atributo de una
 * actualización.
 * {@link com.mypersonalupdates.Update} {@link Filter}
 */
public final class PartialAttributeFilter extends AttributeFilter{
    public static final String DATABASE_TYPE = "PartialAttributeFilter";

    public static PartialAttributeFilter create(Long ID) throws DBException {
        String fieldValue = AttributeFilter.getValueFromID(ID);
        UpdatesProviderAttribute attr = AttributeFilter.getAttributeFromID(ID);

        return new PartialAttributeFilter(ID, attr, fieldValue);
    }

    private PartialAttributeFilter(Long ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public static PartialAttributeFilter create(UpdatesProviderAttribute attr, String value) throws DBException {
        Long filterID;
        filterID = AttributeFilter.create(attr, value, PartialAttributeFilter.DATABASE_TYPE);
        return filterID == null ? null : new PartialAttributeFilter(filterID, attr, value);
    }

    @Override
    public boolean test(Update update) {
        Collection<String> attributeValues = update.getAttributeValues(this.attr);

        String tmp = this.value.toLowerCase();

        if (attributeValues != null)
            for (String value : attributeValues) {
                if (value.toLowerCase().contains(tmp))
                    return true;
            }

        return false;
    }

    @Override
    public JsonElement toJSON() {
        return super.toJSON(PartialAttributeFilter.DATABASE_TYPE);
    }

    @Override
    public void injectSQLConditions(LogSQLQuery query) throws SealedException {
        super.injectSQLConditions(query, "MATCH(#ATTRS_TABLE#.value) AGAINST (? IN BOOLEAN MODE)");
    }
}
