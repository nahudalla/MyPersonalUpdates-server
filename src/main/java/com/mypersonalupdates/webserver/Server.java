package com.mypersonalupdates.webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.log.Restrictions;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.json.InstantSerializerDeserializer;
import com.mypersonalupdates.webserver.json.deserializers.FilterDeserializer;
import com.mypersonalupdates.webserver.json.deserializers.RestrictionsDeserializer;
import com.mypersonalupdates.webserver.json.deserializers.UpdatesProviderAttributeDeserializer;
import com.mypersonalupdates.webserver.json.serializers.ResponseDataSerializer;
import com.mypersonalupdates.webserver.responses.ResponseData;
import com.mypersonalupdates.webserver.rest.RESTResponseProcessor;
import com.mypersonalupdates.webserver.rest.RESTServerErrorHandler;
import com.mypersonalupdates.webserver.websockets.WebSocketStreamEndpoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.ws.rs.core.Response;
import java.time.Instant;

/**
 * Esta clase representa el servidor del sistema
 * que se encarga de recibir y procesar las peticiones
 * de la API REST y del servicio de streming de
 * actualizaciones en tiempo real.
 */
public final class Server {
    public static final Gson JSON = new GsonBuilder()
            .registerTypeAdapter(
                    ResponseData.class,
                    new ResponseDataSerializer()
            )
            .registerTypeAdapter(
                    Filter.class,
                    new FilterDeserializer()
            )
            .registerTypeAdapter(
                    UpdatesProviderAttribute.class,
                    new UpdatesProviderAttributeDeserializer()
            )
            .registerTypeAdapter(
                    Restrictions.class,
                    new RestrictionsDeserializer()
            )
            .registerTypeAdapter(
                    Instant.class,
                    new InstantSerializerDeserializer()
            )
            .create();

    public static final Handler.Builder<Response> REST_HANDLER_BUILDER = new Handler.Builder<>(new RESTResponseProcessor());

    private static final String RESTBasePath = Config.get().getString("webserver.RESTBasePath");

    private static final Server instance = new Server();
    public static Server getInstance() {
        return Server.instance;
    }

    private Server() {}

    public void run(int port) {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();

        HttpConfiguration httpConfig = new HttpConfiguration();

        httpConfig.setSendServerVersion(false);
        httpConfig.setSendDateHeader(false);

        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector httpConnector = new ServerConnector(server, httpFactory);

        httpConnector.setPort(port);

        server.setConnectors(new Connector[]{httpConnector});

        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ctx.setContextPath("/");
        server.setHandler(ctx);

        this.setupREST(ctx);

        try {
            this.setupWebSockets(ctx);

            server.start();
            //server.dumpStdErr();
            server.join();
        } catch (Exception e) {
            // TODO: usar Logger
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }

    private void setupREST(ServletContextHandler context) {
        context.setErrorHandler(new RESTServerErrorHandler());
        ServletHolder serHol = context.addServlet(
                ServletContainer.class,
                Server.RESTBasePath + "*"
        );
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.mypersonalupdates.webserver.rest");
    }

    private void setupWebSockets(ServletContextHandler context) throws ServletException, DeploymentException {
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
        wscontainer.addEndpoint(WebSocketStreamEndpoint.class);
    }
}
