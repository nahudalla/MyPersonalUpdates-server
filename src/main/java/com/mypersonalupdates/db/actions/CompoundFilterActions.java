package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface CompoundFilterActions {

    @SqlUpdate("INSERT INTO compound_filter (ID, filterID1, filterID2, type) VALUES (:ID, :f1, :f2, :type)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("f1") Integer f1,
            @Bind("f2") Integer f2,
            @Bind("type") String type
    );

    @SqlQuery("SELECT ID FROM compound_filter WHERE filterID1 = :f1 AND filterD2 = :f2 AND type = :type LIMIT 1")
    Integer getIDFromContent(
            @Bind("f1") Integer f1,
            @Bind("f2") Integer f2,
            @Bind("type") String type
    );

    @SqlQuery("SELECT type FROM compound_filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT filterID1 FROM compound_filter WHERE ID = :ID AND type = :type")
    Integer getFilterID1FromKeys(
            @Bind("ID") Integer ID,
            @Bind("type") String  type
    );

    @SqlQuery("SELECT filterID1 FROM compound_filter WHERE ID = :ID AND type = :type")
    Integer getFilterID2FromKeys(
            @Bind("ID") Integer ID,
            @Bind("type") String  type
    );
}
