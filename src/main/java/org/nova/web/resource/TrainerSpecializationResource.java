package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.trainer.TrainerSpecializationService;
import org.nova.web.dto.request.trainer.AssignTrainerSpecializationRequest;
import org.nova.web.dto.response.trainer.TrainerSpecializationResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/trainer-specializations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TrainerSpecializationResource {

    private final TrainerSpecializationService trainerSpecializationService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("trainerId") UUID trainerId) {
        List<TrainerSpecializationResponse> list = trainerSpecializationService.findAllResponses(trainerId);
        return Response.ok(ApiResponse.success(200, "Especializaciones de entrenador obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        TrainerSpecializationResponse response = trainerSpecializationService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Especializacion de entrenador obtenida exitosamente", response)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid AssignTrainerSpecializationRequest request) {
        TrainerSpecializationResponse response = trainerSpecializationService.assignSpecialization(request);
        return Response.created(URI.create("/api/v1/trainer-specializations/" + response.id()))
                .entity(ApiResponse.success(201, "Especializacion asignada al entrenador exitosamente", response))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        trainerSpecializationService.deleteTrainerSpecialization(id);
        return Response.noContent().build();
    }
}
