package com.mypersonalupdates.webserver.responses.builders;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.responses.BuilderBase;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase User.
 */
public final class UserResponseBuilder extends BuilderBase<UserResponseBuilder> {
    private final User user;

    public UserResponseBuilder(User user) {
        super("User");
        this.user = user;
    }

    public UserResponseBuilder includeID() {
        this.jsonObject.addProperty("id", this.user.getID());
        return this;
    }

    public UserResponseBuilder includeUsername() throws DBException {
        this.jsonObject.addProperty("username", this.user.getUsername());
        return this;
    }
}
