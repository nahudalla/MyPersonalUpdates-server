package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.ExistsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Iterator;

/**
 * Acciones en la bse de datos para la clase {@link com.mypersonalupdates.users.User}
 */
public interface UserActions {
    @SqlQuery("SELECT user.ID FROM user WHERE user.username = :user AND user.password = :password LIMIT 1")
    Long validateLoginData(
            @Bind("user") String user,
            @Bind("password") String password
    );

    @SqlQuery("SELECT user.ID FROM user WHERE user.ID = :id LIMIT 1")
    Long validateId(
            @Bind("id") long id
    );

    @SqlQuery("SELECT user.ID FROM user WHERE user.username = :user LIMIT 1")
    Long idFromUsername(
            @Bind("user") String username
    );

    @SqlQuery("SELECT user.username FROM user WHERE user.ID = :id LIMIT 1")
    String usernameFromId(
            @Bind("id") Long id
    );

    @SqlUpdate("INSERT INTO user (user.username, user.password) VALUES (:user, :password)")
    int createUser(
            @Bind("user") String username,
            @Bind("password") String password
    );

    @SqlQuery("SELECT category.name FROM category WHERE category.userID = :userID")
    Iterator<String> categoryNamesFromUserID(
            @Bind("userID") Long userID
    );

    @SqlUpdate("DELETE FROM user WHERE user.ID = :userID")
    int removeUserFromID(
            @Bind("userID") Long userID
    );

    @SqlQuery("SELECT value FROM user_updates_provider_attribute WHERE userID = :userID AND providerID = :providerID AND name = :attrName")
    String getProviderAttribute(
            @Bind("userID") Long userID,
            @Bind("providerID") Long providerID,
            @Bind("attrName") String attrName
    );

    @SqlQuery("SELECT userID FROM user_updates_provider_attribute WHERE userID = :userID AND providerID = :providerID AND name = :name")
    @Mapper(ExistsMapper.class)
    boolean existsProviderAttribute(
            @Bind("userID") Long userID,
            @Bind("providerID") Long providerID,
            @Bind("name") String attrName
    );

    @SqlUpdate("INSERT INTO user_updates_provider_attribute (userID, providerID, name, value) VALUES (:userID, :providerID, :name, :value)")
    void insertProviderAttribute(
            @Bind("userID") Long userID,
            @Bind("providerID") Long providerID,
            @Bind("name") String attrName,
            @Bind("value") String value
    );

    @SqlUpdate("UPDATE user_updates_provider_attribute SET value = :value WHERE userID = :userID AND providerID = :providerID AND name = :attrName")
    void setProviderAttribute(
            @Bind("userID") Long userID,
            @Bind("providerID") Long providerID,
            @Bind("attrName") String attrName,
            @Bind("value") String value
    );
}
