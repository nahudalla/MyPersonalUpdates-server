package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface UpdatesProviderAttributeActions {

    @SqlQuery("SELECT filterNotes FROM updates_provider_attribute_association WHERE providerID = :providerID AND attrID = :attrID")
    String getFilterNotes(
            @Bind("providerID") Integer providerID,
            @Bind("attrID")     Integer attrID
    );

    @SqlQuery(  " SELECT updates_provider_attribute.multi " +
                " FROM (updates_provider_attribute_association " +
                "       INNER JOIN updates_provider_attribute" +
                "       ON updates_provider_attribute.ID = updates_provider_attribute_association.attrID)" +
                " WHERE updates_provider_attribute_association.providerID = :providerID " +
                " AND updates_provider_attribute_association.attrID = :attrID")
    Boolean getMulti(
            @Bind("attrID") Integer attrID,
            @Bind("providerID") Integer providerID
    );

    @SqlQuery("SELECT description FROM update_provider_attribute WHERE ID = :ID")
    String getDescription(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT name FROM update_provider_attribute WHERE ID = :ID")
    String getName(
            @Bind("ID") Integer ID
    );
}
