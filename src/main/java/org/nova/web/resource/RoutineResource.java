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
import org.nova.service.workout.RoutineService;
import org.nova.web.dto.request.workout.CreateRoutineRequest;
import org.nova.web.dto.request.workout.UpdateRoutineRequest;
import org.nova.web.dto.response.workout.RoutineResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/routines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RoutineResource {

    private final RoutineService routineService;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("assignmentId") Long assignmentId,
            @QueryParam("memberId") UUID memberId,
            @Context SecurityContext ctx
    ) {
        UUID effectiveMemberId = memberId;
        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("ADMIN")
                && !ctx.isUserInRole("ENTRENADOR") && !ctx.isUserInRole("RECEPCIONISTA")) {
            String username = ctx.getUserPrincipal().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
            effectiveMemberId = user.getId();
        }
        List<RoutineResponse> routines = routineService.findAllResponses(assignmentId, effectiveMemberId);
        return Response.ok(ApiResponse.success(200, "Rutinas obtenidas exitosamente", routines)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        RoutineResponse routine = routineService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Rutina obtenida exitosamente", routine)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response create(@Valid CreateRoutineRequest request) {
        RoutineResponse response = routineService.createRoutine(request);
        return Response.created(URI.create("/api/v1/routines/" + response.id()))
                .entity(ApiResponse.success(201, "Rutina registrada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdateRoutineRequest request) {
        RoutineResponse response = routineService.updateRoutine(id, request);
        return Response.ok(ApiResponse.success(200, "Rutina actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response delete(@PathParam("id") Long id) {
        routineService.deleteRoutine(id);
        return Response.noContent().build();
    }
}
