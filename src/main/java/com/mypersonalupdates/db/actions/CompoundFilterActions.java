package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface CompoundFilterActions {

    @SqlUpdate("INSERT INTO compound_filter (ID, filterID1, filterID2, TYPE) VALUES (:ID, :f1, :f2, :TYPE)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("f1") Integer f1,
            @Bind("f2") Integer f2,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT ID FROM compound_filter WHERE filterID1 = :f1 AND filterD2 = :f2 AND TYPE = :TYPE LIMIT 1")
    Integer getIDFromContent(
            @Bind("f1") Integer f1,
            @Bind("f2") Integer f2,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT TYPE FROM compound_filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT filterID1 FROM compound_filter WHERE ID = :ID")
    Integer getFilterID1FromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT filterID2 FROM compound_filter WHERE ID = :ID")
    Integer getFilterID2FromID(
            @Bind("ID") Integer ID
    );
}
