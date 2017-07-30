package com.mypersonalupdates.webserver.rest.resources;

import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.update.UpdatesSearchHandler;
import com.mypersonalupdates.webserver.rest.RESTRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Esta clase representa el recurso de
 * actualizaciones en la API REST.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("updates")
public class UpdateResource {
    private static final Handler.Builder<Response> BUILDER = Server.REST_HANDLER_BUILDER;

    private static final Handler<Response> updatesSearchHandler = BUILDER.build(new UpdatesSearchHandler<>());

    @POST
    public Response searchUpdates(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            String body
    ) throws RequestProcessingException {
        return updatesSearchHandler.process(
                new RESTRequest.Builder()
                        .setUriInfo(uriInfo)
                        .setHeaders(headers)
                        .setBody(body)
                        .setCanBeEmpty(false)
                        .build()
        );
    }
}
