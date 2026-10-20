package org.nova.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/swagger")
@PermitAll
public class SwaggerRedirectResource {

    @GET
    public Response redirectToScalar() {
        return Response.seeOther(URI.create("/scalar")).build();
    }
}
