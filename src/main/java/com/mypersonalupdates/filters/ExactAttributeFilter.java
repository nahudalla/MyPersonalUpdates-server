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
        Collection<FilterValue> values = new LinkedList<>();
        values.add(new FilterValue(this.value, false));

        return values;

        /*
        No entiendo por qué pasa un UpdatesProviderAttribute por paramentro
        en la implementación pasada, tampoco lo usamos. A demás el padre lo tiene como atributo.
        */
    }

    @Override
    public boolean test(Update update) {
        Collection<String> attributeValues = new LinkedList<>();
        attributeValues.addAll(update.getAttributeValues(this.attr));

        if (attributeValues != null)
            for (String value : attributeValues) {
                if (this.value.equals(value))
                    return true;
            }

        return false;
    }
}
