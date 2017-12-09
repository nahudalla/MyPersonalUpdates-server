package com.mypersonalupdates.providers.reddit;

public class RedditUserResource extends RedditResource {

    public RedditUserResource(RedditUserAuthenticator authenticator, String resourceName) {
        super(authenticator, resourceName);
    }

    @Override
    public String getResourceURL() {
        return "https://www.reddit.com/u/"+this.getResourceName();
    }

    @Override
    protected boolean isUserResource() {
        return true;
    }

    @Override
    protected boolean isSubredditResource() {
        return false;
    }
}
