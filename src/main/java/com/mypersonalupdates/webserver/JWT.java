package com.mypersonalupdates.webserver;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mypersonalupdates.Config;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Esta clase representa un token de acceso generado por el
 * sistema. Se utiliza el estándar JSON Web Tokens (JWT).
 */
public final class JWT {
    private static final long REFRESH_WINDOW = Config.get().getLong("webserver.JWT.refreshWindow");
    private static final String KEY = Config.get().getString("webserver.JWT.signingKey");
    private static final long MAX_AGE = Config.get().getLong("webserver.JWT.maxAge");
    private static final String ISSUER = Config.get().getString("webserver.JWT.issuer");

    private static Algorithm ALGORITHM = null;
    private static JWTVerifier VERIFIER = null;

    static {
        try {
            JWT.ALGORITHM = Algorithm.HMAC512(KEY);
            JWT.VERIFIER = com.auth0.jwt.JWT
                    .require(JWT.ALGORITHM)
                    .withIssuer(JWT.ISSUER)
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Long userID;
    private Instant issuedAt = null;

    public JWT(Long userID) {
        this.userID = userID;
    }

    public JWT(Long userID, Instant issuedAt) {
        this.userID = userID;
        this.issuedAt = issuedAt;
    }

    public static JWT from(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.VERIFIER.verify(token);

        return new JWT(
                Long.valueOf(jwt.getSubject()),
                jwt.getIssuedAt().toInstant()
        );
    }

    private JWT getValidCopy() {
        return new JWT(this.userID);
    }

    public Long getUserID() {
        return this.userID;
    }

    public Instant getIssuedAt() {
        return this.issuedAt;
    }

    public long getTimeLeft() {
        long age = JWT.MAX_AGE - (Instant.now().minus(this.getIssuedAt().getEpochSecond(), ChronoUnit.SECONDS)).getEpochSecond();
        return age >= 0 ? age : 0;
    }

    public long getTimeToRefresh() {
        long ttr = this.getTimeLeft() - JWT.REFRESH_WINDOW;

        return ttr < 0 ? 0 : ttr;
    }

    public boolean isIssued() {
        return this.getIssuedAt() != null;
    }

    public boolean isExpired() {
        return this.isIssued() &&
                Instant.now()
                        .minusSeconds(JWT.MAX_AGE)
                        .isAfter(
                                this.issuedAt
                        );
    }

    public String getToken() throws JWTCreationException {
        if (!this.isIssued())
            this.issuedAt = Instant.now();

        return com.auth0.jwt.JWT.create()
                .withIssuer(JWT.ISSUER)
                .withIssuedAt(Date.from(this.issuedAt))
                .withSubject(this.userID.toString())
                .sign(JWT.ALGORITHM);
    }

    public JWT renew() {
        return this.getValidCopy();
    }

    @Override
    public String toString() {
        return this.getToken();
    }
}
