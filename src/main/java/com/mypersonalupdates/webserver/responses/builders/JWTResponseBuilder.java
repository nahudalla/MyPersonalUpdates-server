package com.mypersonalupdates.webserver.responses.builders;

import com.mypersonalupdates.webserver.JWT;
import com.mypersonalupdates.webserver.responses.BuilderBase;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase JWT.
 */
public final class JWTResponseBuilder extends BuilderBase<JWTResponseBuilder> {
    private final JWT jwt;

    private boolean includeToken, includeTimeLeft, includeTimeToRefresh;

    public JWTResponseBuilder(JWT jwt) {
        super("AccessToken");
        this.jwt = jwt;
    }

    public JWTResponseBuilder includeToken() {
        this.jsonObject.addProperty("token", this.jwt.getToken());
        return this;
    }

    public JWTResponseBuilder includeTimeLeft() {
        this.jsonObject.addProperty("timeLeft", this.jwt.getTimeLeft());
        return this;
    }

    public JWTResponseBuilder includeTimeToRefresh() {
        this.jsonObject.addProperty("timeToRefresh", this.jwt.getTimeToRefresh());
        return this;
    }
}
