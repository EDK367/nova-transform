package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.trainer.SpecializationService;
import org.nova.web.dto.request.trainer.CreateSpecializationRequest;
import org.nova.web.dto.request.trainer.UpdateSpecializationRequest;
import org.nova.web.dto.response.trainer.SpecializationResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/specializations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class SpecializationResource {

    private final SpecializationService specializationService;

    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<SpecializationResponse> specializations = specializationService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Especializaciones obtenidas exitosamente", specializations)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        SpecializationResponse specialization = specializationService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Especializacion obtenida exitosamente", specialization)).build();
    }

    @POST
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "ENTRENADOR", "SPECIALIZATIONS_MANAGE"})
    public Response create(@Valid CreateSpecializationRequest request) {
        SpecializationResponse response = specializationService.createSpecialization(request);
        return Response.created(URI.create("/api/v1/specializations/" + response.id()))
                .entity(ApiResponse.success(201, "Especializacion creada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "ENTRENADOR", "SPECIALIZATIONS_MANAGE"})
    public Response update(@PathParam("id") Long id, @Valid UpdateSpecializationRequest request) {
        SpecializationResponse response = specializationService.updateSpecialization(id, request);
        return Response.ok(ApiResponse.success(200, "Especializacion actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "ENTRENADOR", "SPECIALIZATIONS_MANAGE"})
    public Response delete(@PathParam("id") Long id) {
        specializationService.deleteSpecialization(id);
        return Response.noContent().build();
    }
}
