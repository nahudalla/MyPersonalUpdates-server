package com.mypersonalupdates.webserver;

import com.google.gson.Gson;
import com.mypersonalupdates.webserver.requests.InvalidRequestBodyExeption;
import com.mypersonalupdates.webserver.requests.SparkRequestWrapper;
import com.mypersonalupdates.webserver.responses.Error;
import com.mypersonalupdates.webserver.responses.Message;
import com.mypersonalupdates.webserver.services.LoginService;
import com.mypersonalupdates.webserver.services.LogoutService;
import com.mypersonalupdates.webserver.services.Service;
import com.mypersonalupdates.webserver.services.SignupService;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class HTTPServer {
    private static String RESPONSE_CONTENT_TYPE = "application/json";

    private static HTTPServer ourInstance = new HTTPServer();
    public static HTTPServer getInstance() {
        return HTTPServer.ourInstance;
    }
    private HTTPServer() {}

    private final Gson gson = new Gson();

    public void setup() {
        this.setupErrorHandling();
        this.setupContentType();
        this.setupGzip();
    }

    public void addService(String path, Service service, boolean receivesData)
    {
        if (receivesData)
            Spark.post("/"+path, new RouteHandler(service));
        else
            Spark.get("/"+path, new RouteHandler(service));
    }

    public void addService(String path, Service service)
    {
        this.addService(path, service, false);
    }

    private void setPostRoute(String path, Service service) {
        Spark.post("/"+path, new RouteHandler(service));
    }

    private void setGetRoute(String path, Service service) {
        Spark.get("/"+path, new RouteHandler(service));
    }

    public void setupSessionControl(String[] publicPaths) {
        Spark.before((req, res) -> {
            com.mypersonalupdates.webserver.requests.Request request = new SparkRequestWrapper(req, res);

            boolean isPublicPath = false;
            for(int i = 0; i < publicPaths.length && !isPublicPath; i++)
                if(req.pathInfo().equals("/"+publicPaths[i]))
                    isPublicPath = true;

            if(!LoginService.isUserAuthenticated(request)) {
                if(!isPublicPath)
                    Spark.halt(401, this.gson.toJson(
                            new Message("not-authenticated", "Access is only granted to authenticated users.")
                    ));
            } else {
                if(isPublicPath)
                    Spark.halt(200, this.gson.toJson(
                            new Message("not-available", "Public services can only be accessed by anonymous users.")
                    ));
            }
        });
    }

    private void setupGzip() {
        Spark.after((request, response) -> response.header("Content-Encoding", "gzip"));
    }

    private void setupContentType() {
        Spark.before(((request, response) -> response.type(HTTPServer.RESPONSE_CONTENT_TYPE)));
    }

    private void setupErrorHandling() {
        Spark.notFound(new RouteHandler((req) ->
            new Error(404, "Not found.")
        ));
        Spark.internalServerError(new RouteHandler((req) ->
            new Error(500, "Internal server error.")
        ));
    }

    private class RouteHandler implements Route {
        Service handler;

        RouteHandler(Service handler) {
            this.handler = handler;
        }

        @Override
        public Object handle(Request req, Response res) throws Exception {
            SparkRequestWrapper request = new SparkRequestWrapper(req, res);

            Object errorObj = null;
            Object response = null;

            try {
                response = this.handler.handle(request);
            } catch(InvalidRequestBodyExeption e) {
                // TODO: is this a server error?
                errorObj = new Message("invalid-request", e.getMessage());
            }

            if(errorObj != null)
                return Spark.halt(gson.toJson(errorObj));

            request.close();

            return gson.toJson(response);
        }
    }
}
