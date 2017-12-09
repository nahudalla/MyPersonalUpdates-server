package com.mypersonalupdates.providers.reddit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    public JsonObject fetchData(){
        return this.fetchData(null);
    }

    public JsonObject fetchData(String fromID){
        this.client.setAuth_token(this.authenticator.getAuthToken());
        // TODO : poner const en arch de conf
        String url = this.getResourceURL()+"?limit=100";
        JsonObject jsonObject;
        if (fromID == null){
            jsonObject = client.GET(url);
        } else
            jsonObject = client.GET(url+"&before="+fromID);

        JsonElement aux = jsonObject.get("kind");
        if (aux != null && aux.getAsString().equals("Listing"))
            return jsonObject.get("data") != null ? jsonObject.get("data").getAsJsonObject() : null;

        System.err.println("ERROR al obtener datos de Reddit: "+jsonObject.toString());
        return null;
    }

    protected abstract String getResourceURL();
    protected abstract boolean isUserResource();
    protected abstract boolean isSubredditResource();
}
