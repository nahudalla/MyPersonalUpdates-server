package com.mypersonalupdates;


import com.mypersonalupdates.filters.FilterValue;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public abstract class Filter {

    public boolean remove()
    {
        //TODO: Hacer con base de datos
        System.err.println("remove() no implementado");
        return false;
    }

    public abstract Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider);

    public abstract Collection<FilterValue> getValues(UpdatesProviderAttribute attr);

    public abstract boolean test(Update update);

    public abstract Integer getID();

    //TODO: Cambiar en todo el diagrama los LIST y SET por COLLECTION
}
