package com.mypersonalupdates.filters;


import com.mypersonalupdates.Filter;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.FilterActions;
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

    //TODO: Agregar al diagrama de clases
    protected static Integer create(Filter filter1, Filter filter2, String type) throws DBException {
        Integer filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).compoundFilterGetIDFromContent(
                            filter1.getID(),
                            filter2.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterID != null) {
            filterID = Filter.create("CompoundFilter");

            int rowsAffected = 0;
            final Integer fID = filterID;

            if (filterID != null){
                try {
                    rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(FilterActions.class).createCompoundFilter(
                                    fID,
                                    filter1.getID(),
                                    filter2.getID(),
                                    type
                            )
                    );
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }

            if (rowsAffected <= 0){
                try {
                    DBConnection.getInstance().withHandle(
                            handle -> handle.attach(FilterActions.class).compoundFilterDeleteByID(
                                    fID
                            )
                    );
                } catch (Exception e) {
                    throw new DBException(e);
                }

                filterID = null;
            }
        }

        return filterID;
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
