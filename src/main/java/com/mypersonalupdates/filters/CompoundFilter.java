package com.mypersonalupdates.filters;


import com.google.gson.JsonObject;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.CompoundFilterActions;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

/**
 * Esta clase representa una conjunción lógica abstracta
 * de dos filtros {@link Filter}
 */
public abstract class CompoundFilter extends Filter {
    protected final Filter filter1, filter2;

    public final static String DATABASE_TYPE = "CompoundFilter";

    protected CompoundFilter(Long ID, Filter filter1, Filter filter2) {
        super(ID);
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public boolean remove() throws DBException {
        boolean ret = super.remove();
        this.filter1.remove();
        this.filter2.remove();
        return ret;
    }

    protected static Filter getFilter1FromID(Long ID) throws DBException {
        try {
            Long id = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getFilterID1FromID(
                            ID
                    )
            );

            return id == null ? null : Filter.create(id);

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    protected static Filter getFilter2FromID(Long ID) throws DBException {
        try {
            Long id = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getFilterID2FromID(
                            ID
                    )
            );

            return id == null ? null : Filter.create(id);

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public static CompoundFilter create(Long ID) throws DBException {
        String typeCompound;

        try {
            typeCompound = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getTypeFromID(
                            ID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (typeCompound == null)
            return null;

        if (typeCompound.equals(AndFilter.DATABASE_TYPE))
            return AndFilter.create(ID);

        else if (typeCompound.equals(OrFilter.DATABASE_TYPE))
            return OrFilter.create(ID);

        throw new AssertionError();
    }

    protected static Long create(Filter filter1, Filter filter2, String type) throws DBException {
        Long filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CompoundFilterActions.class).getIDFromContent(
                            filter1.getID(),
                            filter2.getID(),
                            type
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterID == null) {
            filterID = Filter.create(CompoundFilter.DATABASE_TYPE);

            int rowsAffected;
            final Long fID = filterID;

            if (filterID != null) {
                try {
                    rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(CompoundFilterActions.class).create(
                                    fID,
                                    filter1.getID(),
                                    filter2.getID(),
                                    type
                            )
                    );

                    if (rowsAffected <= 0) {
                        Filter.removeFilterByID(filterID);
                        filterID = null;
                    }
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }
        }

        return filterID;
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes(UpdatesProvider provider) {
        Collection<UpdatesProviderAttribute> attributesF1, attributesF2;

        attributesF1 = this.filter1.getAttributes(provider);
        attributesF2 = this.filter2.getAttributes(provider);

        if (attributesF1 != null && attributesF2 != null) {
            attributesF1.addAll(attributesF2);
            return attributesF1;
        }

        if (attributesF1 == null && attributesF2 != null)
            return attributesF2;

        return attributesF1;
    }

    @Override
    public Collection<String> getValues(UpdatesProviderAttribute attr) {
        Collection<String> valuesF1, valuesF2;

        valuesF1 = this.filter1.getValues(attr);
        valuesF2 = this.filter2.getValues(attr);

        if (valuesF1 != null && valuesF2 != null) {
            valuesF1.addAll(valuesF2);
            return valuesF1;
        }

        if (valuesF1 == null && valuesF2 != null)
            return valuesF2;

        return valuesF1;
    }

    @Override
    protected JsonObject toJSON(String type) {
        JsonObject object = super.toJSON(type);
        object.add("filter1", this.filter1.toJSON());
        object.add("filter2", this.filter2.toJSON());
        return object;
    }

    protected void injectSQLConditions(String operator, LogSQLQuery query) throws SealedException {
        query.appendToCondition("(");
        this.filter1.injectSQLConditions(query);
        query.appendToCondition(") "+operator+" (");
        this.filter2.injectSQLConditions(query);
        query.appendToCondition(")");
    }
}
