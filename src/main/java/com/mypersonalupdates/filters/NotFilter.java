package com.mypersonalupdates.filters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.NotFilterActions;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

/**
 * Esta clase representa la negación lógica de un filtro.
 * {@link Filter}
 */
public final class NotFilter extends Filter{
    private final Filter filter;

    public static final String DATABASE_TYPE = "NotFilter";

    private NotFilter(Long ID, Filter filter) {
        super(ID);
        this.filter = filter;
    }

    @Override
    public boolean remove() throws DBException {
        boolean removed = super.remove();
        this.filter.remove();
        return removed;
    }

    public static NotFilter create(Long ID) throws DBException {
        Long filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(NotFilterActions.class).getContentFromID(
                            ID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }



        return filterID == null ? null : new NotFilter(ID, Filter.create(filterID));
    }

    public static NotFilter create(Filter filter) throws DBException {
        Long filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(NotFilterActions.class).getIDFromContent(
                            filter.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterID == null) {
            filterID = Filter.create(NotFilter.DATABASE_TYPE);

            int rowsAffected;
            final Long fID = filterID;

            if (filterID != null) {
                try {
                    rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(NotFilterActions.class).create(
                                    fID,
                                    filter.getID()
                            )
                    );
                    
                    if(rowsAffected <= 0) {
                        Filter.removeFilterByID(filterID);
                        filterID = null;
                    }
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }
        }

        return filterID == null ? null : new NotFilter(filterID, filter);
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributes(UpdatesProvider provider) {
        return null;
    }

    @Override
    public Collection<String> getValues(UpdatesProviderAttribute attr) {
        return null;
    }

    @Override
    public void injectSQLConditions(LogSQLQuery query) throws SealedException {
        query.appendToCondition("NOT (");
        this.filter.injectSQLConditions(query);
        query.appendToCondition(")");
    }

    @Override
    public boolean test(Update update) {
        return !this.filter.test(update);
    }

    @Override
    public JsonElement toJSON() {
        JsonObject object = super.toJSON(NotFilter.DATABASE_TYPE);
        object.add("filter", this.filter.toJSON());
        return object;
    }
}
