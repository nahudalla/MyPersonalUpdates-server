package com.mypersonalupdates;

import com.mypersonalupdates.log.Log;
import com.mypersonalupdates.providers.reddit.RedditProvider;
import com.mypersonalupdates.providers.twitter.TwitterProvider;
import com.mypersonalupdates.webserver.Server;

import java.time.Instant;

public class Main {
    private static final int SERVER_PORT = Config.get().getInt("webserver.port");

    public static void main(String[] args) {
        TwitterProvider.setup();
        RedditProvider.setup();
        Log.setup();
        Server.getInstance().run(Main.SERVER_PORT);

        Instant i = Instant.now().plusSeconds(1000);
    }
}
