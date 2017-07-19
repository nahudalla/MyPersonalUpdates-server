package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.CompoundFilterActions;

public class OrFilter extends CompoundFilter{

    static final String type = "OrFilter";

    //TODO: Agregar al diagrama de clases
    public static OrFilter create(Integer ID) throws DBException {
        Integer filterID1, filterID2;

        try {
            filterID1 = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getFilterID1FromKeys(
                            ID,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        try {
            filterID2 = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getFilterID2FromKeys(
                            ID,
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterID1 != null && filterID2 != null)
            return new OrFilter(ID, Filter.create(filterID1), Filter.create(filterID2));

        return null;
    }

    private OrFilter(Integer ID, Filter filter1, Filter filter2) {
        super(ID, filter1, filter2);
    }

    public OrFilter create(Filter filter1, Filter filter2) throws DBException {
        Integer filterID;
        filterID = CompoundFilter.create(filter1, filter2, type);
        return filterID == null ? null : new OrFilter(filterID, filter1, filter2);
    }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) || this.filter2.test(update);
    }
}
