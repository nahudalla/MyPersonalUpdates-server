package com.mypersonalupdates.webserver.rest.resources;


import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.auth.LoginHandler;
import com.mypersonalupdates.webserver.handlers.auth.TokenRefreshHandler;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de autenticación
 * de usuarios en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("auth")
public class AuthResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> loginHandler = BUILDER.build(new LoginHandler<>());
    private static final Handler<Response> refreshHandler = BUILDER.build(new TokenRefreshHandler<>());

    @POST
    public Response login(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return loginHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .setCanBeEmpty(false)
                        .build()
        );
    }

    @GET
    public Response refresh(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return refreshHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }
}
