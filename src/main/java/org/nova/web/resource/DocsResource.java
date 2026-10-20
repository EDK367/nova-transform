package org.nova.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/docs")
@PermitAll
public class DocsResource {

    @GET
    public Response redirectToScalar() {
        return Response.seeOther(URI.create("/scalar")).build();
    }
}
