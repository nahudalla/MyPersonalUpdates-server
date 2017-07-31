package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * Acciones en la base de datos para la clase {@link com.mypersonalupdates.filters.NotFilter}
 */
public interface NotFilterActions {
    @SqlUpdate("INSERT INTO not_filter (ID, filterID) VALUES (:ID, :filterID)")
    int create(
            @Bind("ID") Long ID,
            @Bind("filterID") Long filterID
    );

    @SqlQuery("SELECT filterID FROM not_filter WHERE ID = :ID LIMIT 1")
    Long getContentFromID(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT ID FROM not_filter WHERE filterID = :filterID LIMIT 1")
    Long getIDFromContent(
            @Bind("filterID") Long filterID
    );
}
