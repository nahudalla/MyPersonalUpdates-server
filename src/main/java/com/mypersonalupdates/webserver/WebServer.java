package com.mypersonalupdates.webserver;

import spark.Spark;

public class WebServer {
    private static WebServer ourInstance = new WebServer();
    public static WebServer getInstance() {
        return ourInstance;
    }
    private WebServer() {}

    private HTTPServer httpServer = HTTPServer.getInstance();
    public HTTPServer getHttpServer() {
        return this.httpServer;
    }

    public void init(int port) {
        Spark.port(port);
        this.httpServer.setup();
    }

    public void init() {
        this.init(8080);
    }

    public void stop() {
        Spark.stop();
    }
}
