package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.LinkedList;

public class AttributeFilter extends Filter{

    private Integer ID;
    private UpdatesProviderAttribute attr;
    private String value;

    private AttributeFilter(Integer ID, UpdatesProviderAttribute attr, String value) {
        this.ID = ID;
        this.attr = attr;
        this.value = value;
    }

    public static AttributeFilter create(UpdatesProviderAttribute attr, String value) {
        //TODO: hacer con la base
        return null;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider) {
        if (this.attr.getProvider().equals(provider)) {
            Collection<UpdatesProviderAttribute> attributes = new LinkedList<>();
            attributes.add(this.attr);
            return attributes;
        }

        return null;
    }

    @Override
    public Collection<String> getValues(UpdatesProviderAttribute attr) {
        Collection<String> values = new LinkedList<>();
        values.add(this.value);
        return values;
    }

    @Override
    public boolean test(Update update) {
        Collection<String> values = update.getAttributeValues(this.attr);
        if (values != null)
            for (String value : values) {
                if (this.value.equals(value))
                    return true;
            }

        return false;
    }

    @Override
    public Integer getID() {
        return this.ID;
    }
}
