package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


public interface FilterActions {

    @SqlUpdate("INSERT INTO filter (type) VALUES (:type)")
    @GetGeneratedKeys
    Integer create(
            @Bind("type") String type
    );

    @SqlQuery("REMOVE FROM filter WHERE ID = :ID LIMIT 1")
    Integer remove(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT type FROM filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

}
