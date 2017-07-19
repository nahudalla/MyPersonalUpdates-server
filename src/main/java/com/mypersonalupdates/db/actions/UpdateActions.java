package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface UpdateActions {

    @SqlQuery("SELECT ID FROM update WHERE ID = :ID")
    Integer getIDFromID(
            @Bind("ID") Integer ID
    );



}
