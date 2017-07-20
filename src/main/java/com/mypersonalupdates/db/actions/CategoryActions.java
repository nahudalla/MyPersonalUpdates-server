package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.ExistsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Collection;
import java.util.Iterator;

public interface CategoryActions {

    @SqlUpdate("INSERT INTO category (userID, name, filterID) VALUES (:userID, :name, :filterID)")
    int create(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("filterID") Integer filterID
    );

    @SqlQuery("SELECT filterID FROM category WHERE userID = :userID AND name = :name")
    Integer getFilterIDFromKeys(
            @Bind("userID") Integer userID,
            @Bind("name") String name
    );

    @SqlUpdate("DELETE FROM category WHERE userID = :userID AND name = :name")
    int remove(
            @Bind("userID") Integer userID,
            @Bind("name") String name
    );

    @SqlUpdate("UPDATE category SET name  = :newName WHERE userID = :userID AND name = :name")
    int setName(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("newName") String newName
    );

    @SqlUpdate("UPDATE category SET filterID  = :newFilterID WHERE userID = :userID AND name = :name")
    int setFilter(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("newFilterID") Integer newFilterID
    );

    @SqlUpdate("INSERT INTO category_provider (userID, name, providerID) VALUES (:userID, :name, :providerID)")
    int addProvider(
            @Bind("userID") Integer ID,
            @Bind("name") String name,
            @Bind("providerID") Integer providerID
    );

    @SqlUpdate("DELETE FROM category_provider WHERE userID = :userID AND name = :name AND providerID = :providerID")
    int removeProvider(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("providerID") Integer providerID
    );

    @SqlQuery("SELECT providerID FROM category_provider WHERE userID = :userID AND name = :name")
    Iterator<Integer> getProvidersFromKeys(
            @Bind("userID") Integer userID,
            @Bind("name") String name
    );

    @SqlQuery("SELECT userID FROM category_provider WHERE userID = :userID AND name = :name AND providerID = :providerID")
    @RegisterMapper(ExistsMapper.class)
    boolean isProviderAssociated(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("providerID") Integer providerID
    );
}
