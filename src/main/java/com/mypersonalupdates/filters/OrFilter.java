package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;

public class OrFilter extends CompoundFilter{

    private OrFilter(Integer ID, Filter filter1, Filter filter2) {
        super(ID, filter1, filter2);
    }

    public OrFilter create(Filter filter1, Filter filter2) throws DBException {
        Integer filterID;
        filterID = CompoundFilter.create(filter1, filter2, "OrFilter");
        return filterID == null ? null : new OrFilter(filterID, filter1, filter2);
    }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) || this.filter2.test(update);
    }
}
