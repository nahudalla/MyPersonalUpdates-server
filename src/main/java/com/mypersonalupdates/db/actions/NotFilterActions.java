package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface NotFilterActions {
    @SqlUpdate("INSERT INTO not_filter (ID, filterID) VALUES (:ID, :filterID)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("filterID") Integer filterID
    );

    @SqlQuery("SELECT ID FROM not_filter WHERE filterID = :filterID LIMIT 1")
    Integer getIDFromContent(
            @Bind("filterID") Integer filterID
    );

    @SqlQuery("REMOVE FROM not_filter WHERE filterID = :filterID LIMIT 1")
    Integer remove(
            @Bind("filterID") Integer filterID
    );
}
