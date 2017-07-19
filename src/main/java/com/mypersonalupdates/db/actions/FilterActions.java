package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


public interface FilterActions {

    @SqlUpdate("INSERT INTO filter (TYPE) VALUES (:TYPE)")
    @GetGeneratedKeys
    Integer create(
            @Bind("TYPE") String type
    );

    @SqlQuery("REMOVE FROM filter WHERE ID = :ID LIMIT 1")
    Integer remove(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT TYPE FROM filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

}
