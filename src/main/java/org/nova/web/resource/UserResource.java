package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.common.PageResponse;
import org.nova.service.user.UserService;
import org.nova.web.dto.request.user.CreateUserRequest;
import org.nova.web.dto.request.user.UpdateUserRequest;
import org.nova.web.dto.response.user.CreateUserResponse;
import org.nova.web.dto.response.user.UserResponse;
import org.nova.web.mapper.UserMapper;

import java.net.URI;
import java.util.UUID;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@RolesAllowed({"SUPER_ADMIN", "ADMIN"})
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDirection,
            @QueryParam("order") String order
    ) {
        String effectiveDirection = (order != null && !order.isBlank()) ? order : sortDirection;
        PageResponse<UserResponse> pagedUsers = userService.findPaged(page, size, sortBy, effectiveDirection);
        return Response.ok(ApiResponse.success(200, "Usuarios obtenidos exitosamente", pagedUsers)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(user -> Response.ok(ApiResponse.success(200, "Usuario obtenido exitosamente", user)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid CreateUserRequest request) {
        CreateUserResponse response = userService.createInternalUser(request);
        return Response.created(URI.create("/api/v1/users/" + response.id()))
                .entity(ApiResponse.success(201, "Usuario creado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid UpdateUserRequest request) {
        UserResponse response = userMapper.toResponse(userService.update(id, request));
        return Response.ok(ApiResponse.success(200, "Usuario actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}
