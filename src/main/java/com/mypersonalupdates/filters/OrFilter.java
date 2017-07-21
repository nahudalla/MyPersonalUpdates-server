package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;

public class OrFilter extends CompoundFilter{

    static final String DATABASE_TYPE = "OrFilter";

    //TODO: Agregar al diagrama de clases
    public static OrFilter create(Integer ID) throws DBException {
        Filter  filter1 = CompoundFilter.getFilter1FromID(ID),
                filter2 = CompoundFilter.getFilter2FromID(ID);

        if (filter1 != null && filter2 != null)
            return new OrFilter(ID, filter1, filter2);

        return null;
    }

    private OrFilter(Integer ID, Filter filter1, Filter filter2) {
        super(ID, filter1, filter2);
    }

    public static OrFilter create(Filter filter1, Filter filter2) throws DBException {
        Integer filterID;
        filterID = CompoundFilter.create(filter1, filter2, NotFilter.DATABASE_TYPE);
        return filterID == null ? null : new OrFilter(filterID, filter1, filter2);
    }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) || this.filter2.test(update);
    }
}
