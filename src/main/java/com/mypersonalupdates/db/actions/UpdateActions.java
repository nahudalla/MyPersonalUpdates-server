package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.Collection;
import java.util.Date;

public interface UpdateActions {

    @SqlQuery("SELECT ID FROM 'update' WHERE ID = :ID")
    Integer getIDFromID(
            @Bind("ID") Integer ID
    );

    @SqlUpdate("INSERT INTO 'update' (providerID, IDFromProvider, timestamp) VALUES (:providerID, :IDFromProvider, :timestamp)")
    @GetGeneratedKeys
    Integer create(
            @Bind("providerID") Integer providerID,
            @Bind("IDFromProvider") String IDFromProvider,
            @Bind("timestamp") Date timestamp
    );

    @SqlQuery("SELECT ID FROM 'update' WHERE providerID = :providerID AND IDFromProvider = :IDFromProvider")
    Integer getIDFromProviderData(
            @Bind("providerID") Integer providerID,
            @Bind("IDFromProvider") String IDFromProvider
    );

    @SqlQuery("SELECT providerID FROM 'update' WHERE ID = :ID")
    Integer getProvider(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT timestamp FROM 'update' WHERE ID = :ID")
    Date getTimestamp(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT IDFromProvider FROM 'update' WHERE ID = :ID")
    String getIDFromProvider(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT value FROM update_attribute WHERE updateID = :updateID AND providerID = :providerID AND attrID = :attrID")
    Collection<String> getAttributeValues(
            @Bind("updateID") Integer updateID,
            @Bind("providerID") Integer providerID,
            @Bind("attrID") Integer attrID
    );

}
