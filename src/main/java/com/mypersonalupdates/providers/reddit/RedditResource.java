package com.mypersonalupdates.providers.reddit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;

public abstract class RedditResource {
    private final RedditUserAuthenticator authenticator;
    private final String resourceName;
    RedditClient client = new RedditClient();

    public RedditResource(RedditUserAuthenticator authenticator, String resourceName) {
        this.authenticator = authenticator;
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public JsonObject fetchData() throws UserNotLoggedInToProviderException {
        return this.fetchData(null);
    }

    public JsonObject fetchData(String fromID) throws UserNotLoggedInToProviderException {
        this.client.setAuth_token(this.authenticator.getAuthToken());
        // TODO : poner const en arch de conf
        String url = this.getResourceURL()+"?limit=100";
        JsonObject jsonObject;
        if (fromID == null){
            jsonObject = this.client.GET(url);
        } else
            jsonObject = this.client.GET(url+"&before="+fromID);

        if(jsonObject != null) {
            JsonElement aux = jsonObject.get("kind");
            if (aux != null && aux.getAsString().equals("Listing"))
                return jsonObject.get("data") != null ? jsonObject.get("data").getAsJsonObject() : null;
            System.err.println("ERROR al obtener datos de Reddit: "+jsonObject.toString());
        } else {
            System.err.println("ERROR al obtener datos de Reddit.");
        }

        return null;
    }

    public String getResourceURL() {
        return this.getRawResourceURL()+".json";
    }

    protected abstract String getRawResourceURL();
    protected abstract boolean isUserResource();
    protected abstract boolean isSubredditResource();
}
