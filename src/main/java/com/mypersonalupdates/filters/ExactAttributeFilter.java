package com.mypersonalupdates.filters;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class ExactAttributeFilter extends AttributeFilter{

    private ExactAttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public ExactAttributeFilter create(UpdatesProviderAttribute attr, String value) {
        //TODO: Hacer con base
        System.err.println("ExactAttributeFilter no está implementado");
        return null;
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

        return attributeValues == null ? null : attributeValues.contains(this.value);
    }
}
