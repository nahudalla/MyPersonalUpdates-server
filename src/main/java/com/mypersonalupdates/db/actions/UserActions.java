package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import javax.xml.ws.BindingType;
import java.util.Iterator;

public interface UserActions {
    @SqlQuery("SELECT user.ID FROM user WHERE user.username = :user AND user.password = :password LIMIT 1")
    Integer validateLoginData(
            @Bind("user") String user,
            @Bind("password") String password
    );

    @SqlQuery("SELECT user.ID FROM user WHERE user.ID = :id LIMIT 1")
    Integer validateId(
            @Bind("id") int id
    );

    @SqlQuery("SELECT user.ID FROM user WHERE user.username = :user LIMIT 1")
    Integer idFromUsername(
            @Bind("user") String username
    );

    @SqlQuery("SELECT user.username FROM user WHERE user.ID = :id LIMIT 1")
    String usernameFromId(
            @Bind("id") Integer id
    );

    @SqlUpdate("INSERT INTO user (user.username, user.password) VALUES (:user, :password)")
    int createUser(
            @Bind("user") String username,
            @Bind("password") String password
    );

    @SqlQuery("SELECT category.name FROM category WHERE category.userID = :userID")
    Iterator<String> categoryNamesFromUserID(
            @Bind("userID") Integer userID
    );

    @SqlQuery("SELECT user.password FROM user WHERE user.ID = :userID LIMIT 1")
    String getPasswordFromID(
            @Bind("userID") Integer userID
    );

    @SqlUpdate("DELETE FROM user WHERE user.ID = :userID")
    int removeUserFromID(
            @Bind("userID") Integer userID
    );

    @SqlQuery("SELECT value FROM user_updates_provider_attribute WHERE userID = :userId AND providerID = :providerID AND name = :attrName")
    String getProviderAttribute(
            @Bind("userID") Integer userID,
            @Bind("providerID") Integer providerID,
            @Bind("attrName") String attrName
    );

    @SqlUpdate("UPDATE user_updates_provider_attribute SET value = :value WHERE userID = :userId AND providerID = :providerID AND name = :attrName")
    void setProviderAttribute(
            @Bind("userID") Integer userID,
            @Bind("providerID") Integer providerID,
            @Bind("attrName") String attrName,
            @Bind("value") String value
    );
}
