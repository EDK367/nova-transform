package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.branch.BranchService;
import org.nova.web.dto.request.branch.CreateBranchRequest;
import org.nova.web.dto.request.branch.UpdateBranchRequest;
import org.nova.web.dto.response.branch.BranchResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/branches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BranchResource {

    private final BranchService branchService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("activeOnly") Boolean activeOnly) {
        List<BranchResponse> branches = branchService.findAllResponses(activeOnly);
        return Response.ok(ApiResponse.success(200, "Sucursales obtenidas exitosamente", branches)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        BranchResponse branch = branchService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Sucursal obtenida exitosamente", branch)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateBranchRequest request) {
        BranchResponse response = branchService.createBranch(request);
        return Response.created(URI.create("/api/v1/branches/" + response.id()))
                .entity(ApiResponse.success(201, "Sucursal creada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdateBranchRequest request) {
        BranchResponse response = branchService.updateBranch(id, request);
        return Response.ok(ApiResponse.success(200, "Sucursal actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        branchService.deleteBranch(id);
        return Response.noContent().build();
    }
}
