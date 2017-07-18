package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface UpdatesProviderAttributeActions {

    @SqlQuery("SELECT ID FROM attribute_filter WHERE attrID = :attrID LIMIT1")
    Integer getIDFromContent(
            @Bind("attrID") Integer attrID
    );

}
