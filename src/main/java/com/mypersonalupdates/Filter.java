package com.mypersonalupdates;


import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.FilterActions;
import com.mypersonalupdates.filters.FilterValue;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;

public abstract class Filter {

    public boolean remove(Integer ID) {
        int rowsAffected = 0;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle ->  handle.attach(FilterActions.class).remove(
                          ID
                    )
            );
        } catch (Exception e) {
            try {
                throw new DBException(e);
            } catch (DBException e1) {
                e1.printStackTrace();
            }
        }

        return rowsAffected == 0 ? false : true;
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

    public abstract Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider);

    public abstract Collection<FilterValue> getValues(UpdatesProviderAttribute attr);

    public abstract boolean test(Update update);

    public abstract Integer getID();

    //TODO: Cambiar en todo el diagrama los LIST y SET por COLLECTION
}
