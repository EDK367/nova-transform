package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.trainer.TrainerAssignmentService;
import org.nova.web.dto.request.trainer.CreateTrainerAssignmentRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerAssignmentRequest;
import org.nova.web.dto.response.trainer.TrainerAssignmentResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/trainer-assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TrainerAssignmentResource {

    private final TrainerAssignmentService trainerAssignmentService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("memberId") UUID memberId,
            @QueryParam("trainerId") UUID trainerId,
            @QueryParam("activeOnly") Boolean activeOnly
    ) {
        List<TrainerAssignmentResponse> list = trainerAssignmentService.findAllResponses(memberId, trainerId, activeOnly);
        return Response.ok(ApiResponse.success(200, "Asignaciones de entrenador obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        TrainerAssignmentResponse response = trainerAssignmentService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Asignacion de entrenador obtenida exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreateTrainerAssignmentRequest request) {
        TrainerAssignmentResponse response = trainerAssignmentService.createAssignment(request);
        return Response.created(URI.create("/api/v1/trainer-assignments/" + response.id()))
                .entity(ApiResponse.success(201, "Asignacion de entrenador creada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdateTrainerAssignmentRequest request) {
        TrainerAssignmentResponse response = trainerAssignmentService.updateAssignment(id, request);
        return Response.ok(ApiResponse.success(200, "Asignacion de entrenador actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        trainerAssignmentService.deleteAssignment(id);
        return Response.noContent().build();
    }
}
