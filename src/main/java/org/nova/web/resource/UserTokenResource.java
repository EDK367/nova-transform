package org.nova.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.user.UserTokenService;
import org.nova.web.dto.request.user.CreateUserTokenRequest;
import org.nova.web.dto.response.user.UserTokenResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/user-tokens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserTokenResource {

    private final UserTokenService userTokenService;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll(@QueryParam("userId") UUID userId) {
        List<UserTokenResponse> tokens = userTokenService.findAllResponses(userId);
        return Response.ok(ApiResponse.success(200, "Tokens de usuario obtenidos exitosamente", tokens)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response getById(@PathParam("id") Long id) {
        UserTokenResponse response = userTokenService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Token obtenido exitosamente", response)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateUserTokenRequest request) {
        UserTokenResponse response = userTokenService.generateToken(request);
        return Response.created(URI.create("/api/v1/user-tokens/" + response.id()))
                .entity(ApiResponse.success(201, "Token de usuario generado exitosamente", response))
                .build();
    }

    @POST
    @Path("/verify/{token}")
    @PermitAll
    public Response verifyAndConsume(@PathParam("token") String token) {
        UserTokenResponse response = userTokenService.consumeToken(token);
        return Response.ok(ApiResponse.success(200, "Token validado y consumido exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        userTokenService.delete(id);
        return Response.noContent().build();
    }
}
