package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.filters.Filter;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * Acciones en la base de datos para la clase {@link Filter}
 */
public interface FilterActions {

    @SqlUpdate("INSERT INTO filter (TYPE) VALUES (:TYPE)")
    @GetGeneratedKeys
    Long create(
            @Bind("TYPE") String type
    );

    @SqlUpdate("DELETE FROM filter WHERE ID = :ID LIMIT 1")
    int remove(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT TYPE FROM filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Long ID
    );

}
