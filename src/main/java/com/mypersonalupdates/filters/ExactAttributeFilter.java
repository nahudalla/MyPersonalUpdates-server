package com.mypersonalupdates.filters;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class ExactAttributeFilter extends AttributeFilter{

    private ExactAttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public ExactAttributeFilter create(UpdatesProviderAttribute attr, String value) throws DBException {
        Integer filterID = null;

        try {
            filterID = AttributeFilter.create(attr, value, "ExactAttributeFilter");
        } catch (DBException e) {
            throw new DBException(e);
        }

        return filterID == null ? null : new ExactAttributeFilter(filterID, attr, value);
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        // TODO: implementar public boolean equals(UpdatesProviderAttribute other) en UpdatesProviderAttribute
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

        return attributeValues == null ? (this.value == null ? true : false) : attributeValues.contains(this.value);
    }
}
