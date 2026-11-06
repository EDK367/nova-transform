package org.nova.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.nova.service.auth.AuthService;
import org.nova.web.dto.request.auth.LoginRequest;

import java.util.HashMap;
import java.util.Map;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService service;
    private final JsonWebToken jwt;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest loginRequest) {
        return Response.ok(service.login(loginRequest)).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "ENTRENADOR", "RECEPCIONISTA", "CLIENTE", "USER"})
    public Response me(@Context SecurityContext ctx) {
        Map<String, Object> body = new HashMap<>();
        body.put("username", ctx.getUserPrincipal() != null ? ctx.getUserPrincipal().getName() : "");
        body.put("subject", jwt.getSubject());
        body.put("groups", jwt.getGroups());
        body.put("role", jwt.getClaim("role"));
        body.put("permissions", jwt.getClaim("permissions"));
        body.put("name", jwt.getClaim("name"));
        body.put("email", jwt.getClaim("email"));
        return Response.ok(body).build();
    }

}
