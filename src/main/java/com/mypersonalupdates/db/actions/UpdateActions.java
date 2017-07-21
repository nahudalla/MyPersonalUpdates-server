package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.ExistsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;

public interface UpdateActions {

    @SqlQuery("SELECT ID FROM 'update' WHERE ID = :ID")
    @RegisterMapper(ExistsMapper.class)
    boolean exists(
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
    List<String> getAttributeValues(
            @Bind("updateID") Integer updateID,
            @Bind("providerID") Integer providerID,
            @Bind("attrID") Integer attrID
    );

}
