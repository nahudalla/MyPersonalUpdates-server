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
        Collection<FilterValue> values = new LinkedList<>();
        values.add(new FilterValue(this.value, true));

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
                if (value.indexOf(value) > -1)
                    return true;
            }

        return false;

        //TODO: Decidir sí respetamos mayúsculas/minusculas o no
        //TODO: Decidir búsqueda de keyword completo o por partes (ver comentario)
        /*
        En esta implementación, se tiene en cuenta el keyword completo, por ejemplo si quiero buscar por "Susana Gimenez"
        y el texto de la actualización tiene sólo "Susana", sólo "Gimenez" o no en el orden que está en el keyword no lo
        tiene en cuenta.
         */
    }
}
