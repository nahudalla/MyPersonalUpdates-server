package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.Collection;

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

    @SqlQuery("REMOVE FROM category WHERE userID = :userID AND name = :name")
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

    @SqlUpdate("UPDATE category SET filterID  = :newFilter WHERE userID = :userID AND name = :name")
    int setFilter(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("newFilter") Integer newFilter
    );

    @SqlUpdate("INSERT INTO category_provider (userID, name, providerID) VALUES (:userID, :name, :providerID)")
    int addProvider(
            @Bind("userID") Integer ID,
            @Bind("name") String name,
            @Bind("providerID") Integer providerID
    );

    @SqlQuery("REMOVE FROM category_provider WHERE userID = :userID AND name = :name AND providerID = :providerID")
    int removeProvider(
            @Bind("userID") Integer userID,
            @Bind("name") String name,
            @Bind("providerID") Integer providerID
    );

    @SqlQuery("SELECT filterID FROM category_provider WHERE userID = :userID AND name = :name")
    Collection<Integer> getProvidersFromKeys(
            @Bind("userID") Integer userID,
            @Bind("name") String name
    );
}
