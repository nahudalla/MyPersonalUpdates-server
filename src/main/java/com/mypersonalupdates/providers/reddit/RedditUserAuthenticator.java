package com.mypersonalupdates.providers.reddit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import okhttp3.Response;

import java.time.Instant;

public class RedditUserAuthenticator {
    private static final String ACCESS_TOKEN_ATTR_NAME = "access_token";
    private static final String REFRESH_TOKEN_ATTR_NAME = "refresh_token";
    private static final String EXPIRE_DATE_ATTR_NAME = "expire_date";

    private final User user;
    private final RedditProvider provider = RedditProvider.getInstance();
    private final RedditClient client = new RedditClient();

    public RedditUserAuthenticator(User user) {
        this.user = user;
    }

    private boolean processTokenResponse(JsonObject response) {
        if(response != null) {
            Long expires_in = null;
            String access_token = null;
            String refresh_token = null;
            String expire_date = null;

            try {
                access_token = response.get("access_token").getAsString();
                expires_in = response.get("expires_in").getAsLong();
                JsonElement element = response.get("refresh_token");
                if(element != null)
                    refresh_token = element.getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(expires_in != null)
                expire_date = String.valueOf(Instant.now().plusSeconds(expires_in).toEpochMilli());

            if(access_token != null && expire_date != null) {
                try {
                    this.user.setAttribute(this.provider, ACCESS_TOKEN_ATTR_NAME, access_token);
                    this.user.setAttribute(this.provider, EXPIRE_DATE_ATTR_NAME, expire_date);
                    if(refresh_token != null)
                        this.user.setAttribute(this.provider, REFRESH_TOKEN_ATTR_NAME, refresh_token);
                    return true;
                } catch (DBException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public boolean setupAuthToken(String code) {
        return this.processTokenResponse(
                this.client.obtainUserToken(code)
        );
    }

    private String refreshAuthToken() {
        String refresh_token = null;
        try {
            refresh_token = this.user.getAttribute(this.provider, REFRESH_TOKEN_ATTR_NAME);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if(refresh_token != null) {
            JsonObject response = this.client.refreshUserToken(refresh_token);

            if (this.processTokenResponse(response)) {
                return response.get("access_token").getAsString();
            }
        }

        return null;
    }

    public String getRedditUserAuthToken() {
        String access_token = null;
        try {
            access_token = this.user.getAttribute(this.provider, ACCESS_TOKEN_ATTR_NAME);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if(access_token != null) {
            String expire_date = null;
            try {
                expire_date = this.user.getAttribute(this.provider, EXPIRE_DATE_ATTR_NAME);
            } catch (DBException e) {
                e.printStackTrace();
            }

            if(expire_date != null) {
                Instant expire_instant;
                try {
                    expire_instant = Instant.ofEpochMilli(Long.valueOf(expire_date));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return null;
                }

                if(Instant.now().isAfter(expire_instant)) {
                    return this.refreshAuthToken();
                }

                return access_token;
            }
        }

        return null;
    }
}
