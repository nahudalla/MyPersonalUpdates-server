package com.mypersonalupdates.webserver.rest.resources;

import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.providers.*;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de un
 * proveedor en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("provider")
public class ProvidersResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> providersListHandler = BUILDER.build(new ProvidersListHandler<>());
    private static final Handler<Response> providerHandler = BUILDER.build(new ProviderHandler<>());
    private static final Handler<Response> providerActionHandler = BUILDER.build(new ProviderActionHandler<>());

    @GET
    @Path("all")
    public Response providersList(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return providersListHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @GET
    @Path("{providerID}")
    public Response providerInfo(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return providerHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @GET
    @Path("{providerID}/action/{actionName}")
    public Response action(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return providerActionHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @POST
    @Path("{providerID}/action/{actionName}")
    public Response action(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return providerActionHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .build()
        );
    }
}
