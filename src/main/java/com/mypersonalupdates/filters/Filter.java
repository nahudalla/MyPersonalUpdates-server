package com.mypersonalupdates.filters;

import com.google.gson.JsonObject;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.FilterActions;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.log.LogSQLQuery;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.json.JSONSerializable;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;

public abstract class Filter implements JSONSerializable {
    private final Long ID;

    protected Filter(Long ID) {
        this.ID = ID;
    }

    public boolean remove() throws DBException {
        int rowsAffected = 0;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).remove(this.ID)
            );
        } catch (Exception e) {
            // La única manera segura de saber si un error fue causado por uno de tipo
            // específico dentro de la cadena de errores.
            // Si la causa del error fue que no se pudo borrar el filtro porque otro
            // registro de la base de datos depende de el, ignorar el error
            boolean ignore = -1 != ExceptionUtils.indexOfThrowable(e, SQLIntegrityConstraintViolationException.class);
            if(!ignore)
                throw new DBException(e);
        }

        return rowsAffected != 0;
    }

    //TODO: Agregar en el diagrama de clases
    protected static Long create(String type) throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(FilterActions.class).create(type)
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    //TODO: Agregar en el diagrama de clases
    public static Filter create(Long ID) throws DBException {
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

        throw new AssertionError();
    }


    public abstract Collection<UpdatesProviderAttribute> getAttributes(UpdatesProvider provider);

    public abstract Collection<String> getValues(UpdatesProviderAttribute attr);

    public abstract void injectSQLConditions(LogSQLQuery query) throws SealedException;

    public abstract boolean test(Update update);

    public Long getID() {
        return this.ID;
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filter)) return false;

        Filter filter = (Filter) o;

        return getID().equals(filter.getID());
    }

    // TODO: agregar al diagrama
    protected static void removeFilterByID(Long filterID) throws DBException {
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

    protected JsonObject toJSON(String type) {
        JsonObject object = new JsonObject();
        object.addProperty("type", type);
        return object;
    }
}
