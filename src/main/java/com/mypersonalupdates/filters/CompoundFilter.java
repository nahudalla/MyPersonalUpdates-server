package com.mypersonalupdates.filters;


import com.mypersonalupdates.Filter;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public abstract class CompoundFilter extends Filter {
    protected Integer ID;
    protected Filter filter1, filter2;

    protected CompoundFilter(Integer ID, Filter filter1, Filter filter2) {
        this.ID = ID;
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider) {
        Collection<UpdatesProviderAttribute> attributesF1, attributesF2;

        attributesF1 = this.filter1.getAttributtes(provider);
        attributesF2 = this.filter2.getAttributtes(provider);

        if (attributesF1 != null && attributesF2 != null ) {
            attributesF1.addAll(attributesF2);
            return attributesF1;
        }

        if (attributesF1 == null && attributesF2 != null)
            return attributesF2;

        return attributesF1;
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        Collection<FilterValue> valuesF1, valuesF2;

        valuesF1 = this.filter1.getValues(attr);
        valuesF2 = this.filter2.getValues(attr);

        if (valuesF1  != null && valuesF2  != null ) {
            valuesF1.addAll(valuesF2 );
            return valuesF1;
        }

        if (valuesF1 == null && valuesF2 != null)
            return valuesF2;

        return valuesF1;
    }

    @Override
    public Integer getID() {
        return this.ID;
    }
}
