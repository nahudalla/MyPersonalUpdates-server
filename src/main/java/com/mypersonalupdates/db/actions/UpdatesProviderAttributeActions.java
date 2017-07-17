package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface UpdatesProviderAttributeActions {

    @SqlQuery("SELECT ID FROM updates_provider_attribute WHERE ID = :ID LIMIT1")
    Integer attributeFilterGetIDFromContent(
            @Bind("ID") Integer ID
    );

}
