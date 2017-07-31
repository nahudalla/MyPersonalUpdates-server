package com.mypersonalupdates.webserver.rest.resources;

import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.user.SignUpHandler;
import com.mypersonalupdates.webserver.handlers.user.UserInfoHandler;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de un
 * usuario en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("user")
public class UserResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> signUpHandler = BUILDER.build(new SignUpHandler<>());
    private static final Handler<Response> userInfoHandler = BUILDER.build(new UserInfoHandler<>());

    @PUT
    public Response create(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return signUpHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .setCanBeEmpty(false)
                        .build()
        );
    }

    @GET
    public Response getInfo(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return userInfoHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }
}
