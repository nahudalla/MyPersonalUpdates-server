package com.mypersonalupdates;

import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.AttributeFilterActions;
import com.mypersonalupdates.db.actions.CompoundFilterActions;
import com.mypersonalupdates.db.actions.FilterActions;
import com.mypersonalupdates.filters.*;
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
        String typeFilter;
        try {
            typeFilter = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).getTypeFromID(
                            ID
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (typeFilter.equals("NotFilter"))
            return NotFilter.create(ID);

        if (typeFilter.equals("CompoundFilter")){
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

            if (typeCompound.equals("AndFilter"))
                return AndFilter.create(ID);

            else if (typeCompound.equals("OrFilter"))
                return OrFilter.create(ID);

            return null;
        }

        if (typeFilter.equals("AttributeFilter")){
            String typeAttribute;
            try {
                typeAttribute = DBConnection.getInstance().withHandle(
                        handle -> handle.attach(AttributeFilterActions.class).getTypeFromID(
                                ID
                        )
                );
            } catch (Exception e) {
                throw new DBException(e);
            }

            if (typeAttribute.equals("ExactAttributeFilter"))
                return ExactAttributeFilter.create(ID);

            else if (typeAttribute.equals("PartialAttributeFilter"))
                return PartialAttributeFilter.create(ID);

            return null;
        }

        return null;
    }

    public abstract Collection<UpdatesProviderAttribute> getAttributtes(UpdatesProvider provider);

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
