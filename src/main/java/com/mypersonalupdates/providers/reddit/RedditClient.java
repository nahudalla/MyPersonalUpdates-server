package com.mypersonalupdates.providers.reddit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;

public class RedditClient {
    private static final String LOGIN_CALLBACK_URL = Config.get().getString("providers.reddit.login_callback_url");
    private static final String APP_CLIENT_ID = Config.get().getString("providers.reddit.clientID");
    private static final String APP_CONSUMER_SECRET = Config.get().getString("providers.reddit.consumerSecret");
    private static final String USER_AGENT = Config.get().getString("providers.reddit.userAgent");

    private final OkHttpClient client = new OkHttpClient();

    private JsonObject doRequest(Request request) {
        Response response;

        try {
            response = this.client.newCall(request).execute();
            if (response.isSuccessful())
                return this.responseToJsonObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Configura un usuario del sistema con las credenciales de Reddit
    public JsonObject obtainUserToken(String code){
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", RedditClient.LOGIN_CALLBACK_URL)
                .build();

        String credentials = Credentials.basic(RedditClient.APP_CLIENT_ID, RedditClient.APP_CONSUMER_SECRET);

        // Builder construye la petición
        Request request = new Request.Builder()
                .url("https://www.reddit.com/api/v1/access_token")
                .header("Authorization", credentials)
                .header("User-Agent", RedditClient.USER_AGENT)
                .post(body)
                .build();

        return this.doRequest(request);
    }

    public JsonObject refreshUserToken(String refresh_token) {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refresh_token)
                .build();

        String credentials = Credentials.basic(RedditClient.APP_CLIENT_ID, RedditClient.APP_CONSUMER_SECRET);

        // Builder construye la petición
        Request request = new Request.Builder()
                .url("https://www.reddit.com/api/v1/access_token")
                .header("Authorization", credentials)
                .post(body)
                .build();

        return this.doRequest(request);
    }

    private JsonObject responseToJsonObject(Response response) {
        JsonParser parser = new JsonParser();

        JsonElement element = null;
        try {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                element = parser.parse(responseBody.string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        if (element != null && element.isJsonObject())
            return element.getAsJsonObject();

        return null;
    }
}
