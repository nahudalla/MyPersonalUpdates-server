package com.mypersonalupdates.filters;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class ExactAttributeFilter extends AttributeFilter{

    public static final String DATABASE_TYPE = "ExactAttributeFilter";

    public static ExactAttributeFilter create(Integer ID) throws DBException {
        String fieldValue = AttributeFilter.getValueFromID(ID);
        UpdatesProviderAttribute attr = AttributeFilter.getAttributeFromID(ID);

        if(fieldValue != null && attr != null)
            return new ExactAttributeFilter(ID, attr, fieldValue);
        
        return null;
    }

    private ExactAttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public static ExactAttributeFilter create(UpdatesProviderAttribute attr, String value) throws DBException {
        Integer filterID;
        filterID = AttributeFilter.create(attr, value, ExactAttributeFilter.DATABASE_TYPE);
        return filterID == null ? null : new ExactAttributeFilter(filterID, attr, value);
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        if(this.attr.equals(attr)) {
            Collection<FilterValue> values = new LinkedList<>();
            values.add(new FilterValue(this.value, false));
            return values;
        }
        return null;
    }

    @Override
    public boolean test(Update update) {
        Collection<String> attributeValues = update.getAttributeValues(this.attr);

        return attributeValues == null ? (this.value == null) : attributeValues.contains(this.value);
    }
}
