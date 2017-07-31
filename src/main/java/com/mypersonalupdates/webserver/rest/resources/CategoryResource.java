package com.mypersonalupdates.webserver.rest.resources;

import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.category.*;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de categoría
 * en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("category")
public class CategoryResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> creationHandler = BUILDER.build(new CategoryCreationHandler<>());
    private static final Handler<Response> updateHandler = BUILDER.build(new CategoryUpdateHandler<>());
    private static final Handler<Response> removeHandler = BUILDER.build(new CategoryRemoveHandler<>());
    private static final Handler<Response> getHandler = BUILDER.build(new CategoryHandler<>());
    private static final Handler<Response> getAllHandler = BUILDER.build(new CategoryListHandler<>());

    @GET
    public Response getAll(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return getAllHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @PUT
    @Path("{categoryName}")
    public Response create(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return creationHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .setCanBeEmpty(false)
                        .build()
        );
    }

    @POST
    @Path("{categoryName}")
    public Response update(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return updateHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .setCanBeEmpty(false)
                        .build()
        );
    }

    @DELETE
    @Path("{categoryName}")
    public Response remove(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return removeHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @GET
    @Path("{categoryName}")
    public Response getCategory(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return getHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }
}
