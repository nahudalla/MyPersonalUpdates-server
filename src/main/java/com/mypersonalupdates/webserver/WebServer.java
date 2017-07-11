package com.mypersonalupdates.webserver;

import com.mypersonalupdates.webserver.requests.Request;
import com.mypersonalupdates.webserver.responses.Message;
import com.mypersonalupdates.webserver.services.LoginService;
import com.mypersonalupdates.webserver.services.LogoutService;
import com.mypersonalupdates.webserver.services.SignupService;
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

        this.httpServer.setupSessionControl(new String[]{
                "login",
                "signup"
        });

        this.httpServer.addService("login", new LoginService(), true);
        this.httpServer.addService("signup", new SignupService(), true);
        this.httpServer.addService("logout", new LogoutService());
        this.httpServer.addService("ping", (Request request) -> new Message("pong"));
    }

    public void init() {
        this.init(8080);
    }

    public void stop() {
        Spark.stop();
    }
}


