package com.mypersonalupdates.filters;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class PartialAttributeFilter extends AttributeFilter{

    public static final String DATABASE_TYPE = "PartialAttributeFilter";

    public static PartialAttributeFilter create(Integer ID) throws DBException {
        String fieldValue = AttributeFilter.getValueFromID(ID);
        UpdatesProviderAttribute attr = AttributeFilter.getAttributeFromID(ID);

        return new PartialAttributeFilter(ID, attr, fieldValue);
    }

    private PartialAttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value.toLowerCase());
    }

    public static PartialAttributeFilter create(UpdatesProviderAttribute attr, String value) throws DBException {
        Integer filterID;
        filterID = AttributeFilter.create(attr, value, PartialAttributeFilter.DATABASE_TYPE);
        return filterID == null ? null : new PartialAttributeFilter(filterID, attr, value);
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        if(this.attr.equals(attr)) {
            Collection<FilterValue> values = new LinkedList<>();
            values.add(new FilterValue(this.value, true));
            return values;
        }
        return null;
    }

    @Override
    public boolean test(Update update) {
        Collection<String> attributeValues = update.getAttributeValues(this.attr);

        if (attributeValues != null)
            for (String value : attributeValues) {
                if (value.toLowerCase().contains(this.value))
                    return true;
            }

        return false;
    }
}
