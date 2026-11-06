package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.user.RoleService;
import org.nova.web.dto.request.role.AssignRolePermissionsRequest;
import org.nova.web.dto.request.role.CreateRoleRequest;
import org.nova.web.dto.request.role.UpdateRoleRequest;
import org.nova.web.dto.response.role.RoleResponse;
import org.nova.web.mapper.RoleMapper;

import java.net.URI;
import java.util.List;

@Path("/api/v1/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RoleResource {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response getAll() {
        List<RoleResponse> roles = roleMapper.toResponseList(roleService.findAll());
        return Response.ok(ApiResponse.success(200, "Roles obtenidos exitosamente", roles)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response getById(@PathParam("id") Long id) {
        return roleService.findById(id)
                .map(roleMapper::toResponse)
                .map(role -> Response.ok(ApiResponse.success(200, "Rol obtenido exitosamente", role)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed("SUPER_ADMIN")
    public Response create(@Valid CreateRoleRequest request) {
        RoleResponse created = roleMapper.toResponse(roleService.createRole(request));
        return Response.created(URI.create("/api/v1/roles/" + created.id()))
                .entity(ApiResponse.success(201, "Rol creado exitosamente", created))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("SUPER_ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdateRoleRequest request) {
        RoleResponse updated = roleMapper.toResponse(roleService.updateRole(id, request));
        return Response.ok(ApiResponse.success(200, "Rol actualizado exitosamente", updated)).build();
    }

    @PUT
    @Path("/{id}/permissions")
    @RolesAllowed("SUPER_ADMIN")
    public Response updatePermissions(@PathParam("id") Long id, @Valid AssignRolePermissionsRequest request) {
        RoleResponse updated = roleMapper.toResponse(roleService.updateRolePermissions(id, request != null ? request.permissionIds() : List.of()));
        return Response.ok(ApiResponse.success(200, "Permisos del rol actualizados exitosamente", updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("SUPER_ADMIN")
    public Response delete(@PathParam("id") Long id) {
        roleService.deleteRole(id);
        return Response.noContent().build();
    }
}
