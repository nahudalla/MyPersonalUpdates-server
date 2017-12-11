package com.mypersonalupdates.filters;

import com.google.gson.JsonElement;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;

/**
 * Esta clase representa la conjunción mediante el
 * operador lógico OR de dos filtros {@link Filter}
 */
public final class OrFilter extends CompoundFilter{
    public static final String DATABASE_TYPE = "OrFilter";

    public static OrFilter create(Long ID) throws DBException {
        Filter  filter1 = CompoundFilter.getFilter1FromID(ID),
                filter2 = CompoundFilter.getFilter2FromID(ID);

        if (filter1 != null && filter2 != null)
            return new OrFilter(ID, filter1, filter2);

        return null;
    }

    private OrFilter(Long ID, Filter filter1, Filter filter2) {
        super(ID, filter1, filter2);
    }

    public static OrFilter create(Filter filter1, Filter filter2) throws DBException {
        Long filterID;
        filterID = CompoundFilter.create(filter1, filter2, OrFilter.DATABASE_TYPE);
        return filterID == null ? null : new OrFilter(filterID, filter1, filter2);
    }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) || this.filter2.test(update);
    }

    @Override
    public void injectSQLConditions(LogSQLQuery query) throws SealedException {
        super.injectSQLConditions("OR", query);
    }

    @Override
    public JsonElement toJSON() {
        return super.toJSON(OrFilter.DATABASE_TYPE);
    }
}
