package com.mypersonalupdates;

import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.FilterActions;
import com.mypersonalupdates.filters.AttributeFilter;
import com.mypersonalupdates.filters.CompoundFilter;
import com.mypersonalupdates.filters.FilterValue;
import com.mypersonalupdates.filters.NotFilter;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public abstract class Filter {

    public boolean remove(Integer ID) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle ->  handle.attach(FilterActions.class).remove(
                          ID
                    )
            );
        } catch (Exception e) {
                throw new DBException(e);
            }

        return rowsAffected != 0;
    }

    //TODO: Agregar en el diagrama de clases
    protected static Integer create(String type) throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).create(type)
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar en el diagrama de clases
    public static Filter create (Integer ID) throws DBException {
        String filterType;
        try {
            filterType = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).getTypeFromID(
                            ID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (filterType == null)
            return null;

        if (filterType.equals(NotFilter.DATABASE_TYPE))
            return NotFilter.create(ID);

        if (filterType.equals(CompoundFilter.DATABASE_TYPE))
            return CompoundFilter.create(ID);

        if (filterType.equals(AttributeFilter.DATABASE_TYPE))
            return AttributeFilter.create(ID);

        return null;
    }

    public abstract Collection<UpdatesProviderAttribute> getAttributes(UpdatesProvider provider);

    public abstract Collection<FilterValue> getValues(UpdatesProviderAttribute attr);

    public abstract boolean test(Update update);

    public abstract Integer getID();

    // TODO: agregar al diagrama
    protected static void removeFilterByID(Integer filterID) throws DBException {
        try {
            DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).remove(
                            filterID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Cambiar en todo el diagrama los LIST y SET por COLLECTION
}
