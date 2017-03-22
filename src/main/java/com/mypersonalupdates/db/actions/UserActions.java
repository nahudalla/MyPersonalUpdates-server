package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserActions {
    @SqlQuery("SELECT users.id FROM users WHERE users.user = :user AND users.password = :password LIMIT 1")
    Integer validateLoginData(
            @Bind("user") String user,
            @Bind("password") String password
    );

    @SqlQuery("SELECT users.id FROM users WHERE users.id = :id LIMIT 1")
    Integer validateId(
            @Bind("id") int id
    );

    @SqlQuery("SELECT users.id FROM users WHERE users.user = :user LIMIT 1")
    Integer idFromUsername(
            @Bind("user") String username
    );

    @SqlQuery("SELECT users.user FROM users WHERE users.id = :id LIMIT 1")
    String usernameFromId(
            @Bind("id") Integer id
    );

    @SqlUpdate("INSERT INTO users (users.user, users.password) VALUES (:user, :password)")
    int createUser(
            @Bind("user") String username,
            @Bind("password") String password
    );
}
