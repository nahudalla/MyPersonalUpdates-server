package com.mypersonalupdates.filters;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class PartialAttributeFilter extends AttributeFilter{

    private PartialAttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        super(ID, attr, value);
    }

    public PartialAttributeFilter create(UpdatesProviderAttribute attr, String value) {
        //TODO: Hacer con base
        System.err.println("PartialAttributeFilter no está implementado");
        return null;
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        // TODO: implementar public boolean equals(UpdatesProviderAttribute other) en UpdatesProviderAttribute
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
