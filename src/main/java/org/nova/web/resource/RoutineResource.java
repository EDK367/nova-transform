package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
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

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("assignmentId") Long assignmentId,
            @QueryParam("memberId") UUID memberId
    ) {
        List<RoutineResponse> routines = routineService.findAllResponses(assignmentId, memberId);
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
