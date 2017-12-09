package com.mypersonalupdates.providers.reddit;

public class RedditSubredditResource extends RedditResource {

    public RedditSubredditResource(RedditUserAuthenticator authenticator, String resourceName) {
        super(authenticator, resourceName);
    }

    @Override
    public String getResourceURL() {
        return "https://www.reddit.com/r/"+this.getResourceName();
    }

    @Override
    protected boolean isUserResource() {
        return false;
    }

    @Override
    protected boolean isSubredditResource() {
        return true;
    }


}
