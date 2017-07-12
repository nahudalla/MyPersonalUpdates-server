package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public class NotFilter extends Filter{
    private Integer ID;
    private Filter filter;

    private NotFilter(Integer ID, Filter filter) {
        this.ID = ID;
        this.filter = filter;
    }

    public static NotFilter create(Filter filter) {
        //TODO: Hacer con la base
        return null;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider) {
        return null;
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        return null;
    }

    @Override
    public boolean test(Update update) {
        return !this.filter.test(update);
    }

    @Override
    public Integer getID() {
        return this.ID;
    }
}
