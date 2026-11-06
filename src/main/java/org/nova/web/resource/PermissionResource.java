package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.user.PermissionService;
import org.nova.web.dto.request.permission.CreatePermissionRequest;
import org.nova.web.dto.request.permission.UpdatePermissionRequest;
import org.nova.web.dto.response.permission.PermissionResponse;
import org.nova.web.mapper.PermissionMapper;

import java.net.URI;
import java.util.List;

@Path("/api/v1/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PermissionResource {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response getAll(@QueryParam("module") String module) {
        List<PermissionResponse> permissions = permissionMapper.toResponseList(permissionService.findAll(module));
        return Response.ok(ApiResponse.success(200, "Permisos obtenidos exitosamente", permissions)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response getById(@PathParam("id") Long id) {
        return permissionService.findById(id)
                .map(permissionMapper::toResponse)
                .map(perm -> Response.ok(ApiResponse.success(200, "Permiso obtenido exitosamente", perm)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed("SUPER_ADMIN")
    public Response create(@Valid CreatePermissionRequest request) {
        PermissionResponse created = permissionMapper.toResponse(permissionService.create(request));
        return Response.created(URI.create("/api/v1/permissions/" + created.id()))
                .entity(ApiResponse.success(201, "Permiso creado exitosamente", created))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("SUPER_ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdatePermissionRequest request) {
        PermissionResponse updated = permissionMapper.toResponse(permissionService.update(id, request));
        return Response.ok(ApiResponse.success(200, "Permiso actualizado exitosamente", updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("SUPER_ADMIN")
    public Response delete(@PathParam("id") Long id) {
        permissionService.delete(id);
        return Response.noContent().build();
    }
}
