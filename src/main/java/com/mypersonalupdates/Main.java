package com.mypersonalupdates;

import com.mypersonalupdates.log.Log;
import com.mypersonalupdates.providers.twitter.TwitterProvider;
import com.mypersonalupdates.webserver.Server;

public class Main {
    private static final int SERVER_PORT = Config.get().getInt("webserver.port");

    public static void main(String[] args) {
        TwitterProvider.setup();
        Log.setup();
        Server.getInstance().run(Main.SERVER_PORT);
    }
}