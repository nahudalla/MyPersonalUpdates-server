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
    private static final String APP_CONSUMER_SECRET = Config.get().getString("providers.redditconsumerSecret");

    private static final RedditClient instance = new RedditClient();

    public static RedditClient getInstance() {
        return RedditClient.instance;
    }

    private Response doRequest(Request request) {
        OkHttpClient client = new OkHttpClient();
        Response response;

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful())
                return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Configura un usuario del sistema con las credenciales de Reddit
    public boolean setupUser(User user, String code){
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
                .post(body)
                .build();

        Response response = this.doRequest(request);

        if (response != null) {
            JsonParser parser = new JsonParser();

            JsonElement element = null;
            try {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    element = parser.parse(responseBody.string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (element != null && element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                String access_token;
                String refresh_token;
                long expires_in;

                try {
                    access_token = obj.get("access_token").getAsString();
                    refresh_token = obj.get("refresh_token").getAsString();
                    expires_in = obj.get("expires_in").getAsLong();
                } catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

                String expire_date = String.valueOf(Instant.now().plusSeconds(expires_in).toEpochMilli());

                try {
                    user.setAttribute(RedditProvider.getInstance(), "access_token", access_token);
                    user.setAttribute(RedditProvider.getInstance(), "refresh_token", refresh_token);
                    user.setAttribute(RedditProvider.getInstance(), "expire_date", expire_date);
                } catch (DBException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
        }

        return false;
    }
}
