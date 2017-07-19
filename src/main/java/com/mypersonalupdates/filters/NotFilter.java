package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.NotFilterActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public class NotFilter extends Filter{
    private Integer ID;
    private Filter filter;

    static final String DATABASE_TYPE = "NotFilter";

    private NotFilter(Integer ID, Filter filter) {
        this.ID = ID;
        this.filter = filter;
    }

    //TODO: agregar al diagrama de clases
    public static NotFilter create(Integer filterID) throws DBException {
        Integer notFilterID;

        try {
            notFilterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(NotFilterActions.class).getIDFromContent(
                            filterID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return notFilterID == null ? null : new NotFilter(notFilterID, Filter.create(filterID));
    }

    public static NotFilter create(Filter filter) throws DBException {
        Integer filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(NotFilterActions.class).getIDFromContent(
                            filter.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        boolean okCreate = true;

        if (filterID == null) {
            filterID = Filter.create(DATABASE_TYPE);

            int rowsAffected = 0;
            final Integer fID = filterID;

            if (filterID != null) {
                try {
                    rowsAffected = DBConnection.getInstance().withHandle(
                            handle -> handle.attach(NotFilterActions.class).create(
                                    fID,
                                    filter.getID()
                            )
                    );
                } catch (Exception e) {
                    throw new DBException(e);
                }
            }

            if(rowsAffected <= 0) {
                Filter.removeFilterByID(filterID);
                filterID = null;
            }
        }

        return filterID == null ? null : new NotFilter(filterID, filter);
    }

    @Override
    public Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider) {
        return null;
    }

    @Override
    public Collection<FilterValue> getValues(UpdatesProviderAttribute attr) {
        return null;
    }

    @Override
    public boolean test(Update update) {
        return !this.filter.test(update);
    }

    @Override
    public Integer getID() {
        return this.ID;
    }
}
