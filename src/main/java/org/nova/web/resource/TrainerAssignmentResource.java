package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.domain.model.user.User;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
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
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("memberId") UUID memberId,
            @QueryParam("trainerId") UUID trainerId,
            @QueryParam("activeOnly") Boolean activeOnly,
            @Context SecurityContext ctx
    ) {
        UUID effectiveMemberId = memberId;
        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("SUPER_ADMIN") && !ctx.isUserInRole("ADMIN")
                && !ctx.isUserInRole("ENTRENADOR") && !ctx.isUserInRole("RECEPCIONISTA")) {
            String username = ctx.getUserPrincipal().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
            effectiveMemberId = user.getId();
        }
        List<TrainerAssignmentResponse> list = trainerAssignmentService.findAllResponses(effectiveMemberId, trainerId, activeOnly);
        return Response.ok(ApiResponse.success(200, "Asignaciones de entrenador obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        TrainerAssignmentResponse response = trainerAssignmentService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Asignacion de entrenador obtenida exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "ASSIGNMENTS_MANAGE"})
    public Response create(@Valid CreateTrainerAssignmentRequest request) {
        TrainerAssignmentResponse response = trainerAssignmentService.createAssignment(request);
        return Response.created(URI.create("/api/v1/trainer-assignments/" + response.id()))
                .entity(ApiResponse.success(201, "Asignacion de entrenador creada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "ASSIGNMENTS_MANAGE"})
    public Response update(@PathParam("id") Long id, @Valid UpdateTrainerAssignmentRequest request) {
        TrainerAssignmentResponse response = trainerAssignmentService.updateAssignment(id, request);
        return Response.ok(ApiResponse.success(200, "Asignacion de entrenador actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "ASSIGNMENTS_MANAGE"})
    public Response delete(@PathParam("id") Long id) {
        trainerAssignmentService.deleteAssignment(id);
        return Response.noContent().build();
    }
}
