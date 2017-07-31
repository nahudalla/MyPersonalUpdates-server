package com.mypersonalupdates.webserver.rest.resources;

import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.providers.ProviderAttributeHandler;
import com.mypersonalupdates.webserver.handlers.providers.ProviderAttributesListHandler;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de atributo de
 * un proveedor en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("provider")
public class ProviderAttributesResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> providerAttributeListHandler = BUILDER.build(new ProviderAttributesListHandler<>());
    private static final Handler<Response> providerAttributeHandler = BUILDER.build(new ProviderAttributeHandler<>());

    @GET
    @Path("{providerID}/attribute/all")
    public Response providerAttributes(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return providerAttributeListHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }

    @GET
    @Path("{providerID}/attribute/{attrID}")
    public Response providerAttribute(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers
    ) throws RequestProcessingException {
        return providerAttributeHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .build()
        );
    }
}
