package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.user.RoleService;
import org.nova.web.dto.response.role.RoleResponse;
import org.nova.web.mapper.RoleMapper;

import java.util.List;

@Path("/api/v1/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@RolesAllowed("ADMIN")
public class RoleResource {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GET
    public Response getAll() {
        List<RoleResponse> roles = roleMapper.toResponseList(roleService.findAll());
        return Response.ok(ApiResponse.success(200, "Roles obtenidos exitosamente", roles)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return roleService.findById(id)
                .map(roleMapper::toResponse)
                .map(role -> Response.ok(ApiResponse.success(200, "Rol obtenido exitosamente", role)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
