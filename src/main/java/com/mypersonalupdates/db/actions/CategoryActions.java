package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.CategoryMapper;
import com.mypersonalupdates.db.mappers.ExistsMapper;
import com.mypersonalupdates.users.Category;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Iterator;

/**
 * Acciones en la base de datos par la clase {@link Category}
 */
public interface CategoryActions {
    @SqlUpdate("INSERT INTO category (userID, name, filterID) VALUES (:userID, :name, :filterID)")
    int create(
            @Bind("userID") Long userID,
            @Bind("name") String name,
            @Bind("filterID") Long filterID
    );

    @SqlQuery("SELECT filterID FROM category WHERE userID = :userID AND name = :name")
    Long getFilterIDFromKeys(
            @Bind("userID") Long userID,
            @Bind("name") String name
    );

    @SqlUpdate("DELETE FROM category WHERE userID = :userID AND name = :name")
    int remove(
            @Bind("userID") Long userID,
            @Bind("name") String name
    );

    @SqlUpdate("UPDATE category SET name  = :newName WHERE userID = :userID AND name = :name")
    int setName(
            @Bind("userID") Long userID,
            @Bind("name") String name,
            @Bind("newName") String newName
    );

    @SqlUpdate("UPDATE category SET filterID  = :newFilterID WHERE userID = :userID AND name = :name")
    int setFilter(
            @Bind("userID") Long userID,
            @Bind("name") String name,
            @Bind("newFilterID") Long newFilterID
    );

    @SqlUpdate("INSERT INTO category_provider (userID, name, providerID) VALUES (:userID, :name, :providerID)")
    int addProvider(
            @Bind("userID") Long ID,
            @Bind("name") String name,
            @Bind("providerID") Long providerID
    );

    @SqlUpdate("DELETE FROM category_provider WHERE userID = :userID AND name = :name AND providerID = :providerID")
    int removeProvider(
            @Bind("userID") Long userID,
            @Bind("name") String name,
            @Bind("providerID") Long providerID
    );

    @SqlQuery("SELECT providerID FROM category_provider WHERE userID = :userID AND name = :name")
    Iterator<Long> getProvidersFromKeys(
            @Bind("userID") Long userID,
            @Bind("name") String name
    );

    @SqlQuery("SELECT userID FROM category_provider WHERE userID = :userID AND name = :name AND providerID = :providerID")
    @RegisterMapper(ExistsMapper.class)
    boolean isProviderAssociated(
            @Bind("userID") Long userID,
            @Bind("name") String name,
            @Bind("providerID") Long providerID
    );

    @SqlQuery("SELECT userID, name FROM category")
    @RegisterMapper(CategoryMapper.class)
    Iterator<Category> getAllCategories();
}
