package com.mypersonalupdates.filters;

import com.google.gson.JsonElement;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;

/**
 * Esta clase representa la conjunción mediante el
 * operador lógico AND de dos filtros {@link Filter}
 */
public final class AndFilter extends CompoundFilter {
    public static final String DATABASE_TYPE = "AndFilter";

    public static AndFilter create(Long ID) throws DBException {
        Filter filter1 = CompoundFilter.getFilter1FromID(ID),
                filter2 = CompoundFilter.getFilter2FromID(ID);

        if (filter1 != null && filter2 != null)
            return new AndFilter(ID, filter1, filter2);

        return null;
    }

    private AndFilter(Long ID, Filter filter1, Filter filter2) {
        super(ID, filter1, filter2);
    }

    public static AndFilter create(Filter filter1, Filter filter2) throws DBException {
        Long filterID;
        filterID = CompoundFilter.create(filter1, filter2, AndFilter.DATABASE_TYPE);
        return filterID == null ? null : new AndFilter(filterID, filter1, filter2);
    }

    @Override
    public void injectSQLConditions(LogSQLQuery query) throws SealedException {
        super.injectSQLConditions("AND", query);
    }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) && this.filter2.test(update);
    }

    @Override
    public JsonElement toJSON() {
        return super.toJSON(AndFilter.DATABASE_TYPE);
    }
}
