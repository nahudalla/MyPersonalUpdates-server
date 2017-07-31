package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * Acciones en la base de datos para la clase {@link com.mypersonalupdates.filters.CompoundFilter}
 */
public interface CompoundFilterActions {
    @SqlUpdate("INSERT INTO compound_filter (ID, filterID1, filterID2, TYPE) VALUES (:ID, :f1, :f2, :TYPE)")
    int create(
            @Bind("ID") Long ID,
            @Bind("f1") Long f1,
            @Bind("f2") Long f2,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT ID FROM compound_filter WHERE filterID1 = :f1 AND filterID2 = :f2 AND TYPE = :TYPE LIMIT 1")
    Long getIDFromContent(
            @Bind("f1") Long f1,
            @Bind("f2") Long f2,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT TYPE FROM compound_filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT filterID1 FROM compound_filter WHERE ID = :ID")
    Long getFilterID1FromID(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT filterID2 FROM compound_filter WHERE ID = :ID")
    Long getFilterID2FromID(
            @Bind("ID") Long ID
    );
}
