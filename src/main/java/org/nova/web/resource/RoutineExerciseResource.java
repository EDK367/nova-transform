package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.workout.RoutineExerciseService;
import org.nova.web.dto.request.workout.CreateRoutineExerciseRequest;
import org.nova.web.dto.request.workout.UpdateRoutineExerciseRequest;
import org.nova.web.dto.response.workout.RoutineExerciseResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/routine-exercises")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class RoutineExerciseResource {

    private final RoutineExerciseService routineExerciseService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("routineId") Long routineId,
            @QueryParam("trainingDay") String trainingDay
    ) {
        List<RoutineExerciseResponse> list = routineExerciseService.findAllResponses(routineId, trainingDay);
        return Response.ok(ApiResponse.success(200, "Ejercicios de rutina obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        RoutineExerciseResponse response = routineExerciseService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Ejercicio de rutina obtenido exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response create(@Valid CreateRoutineExerciseRequest request) {
        RoutineExerciseResponse response = routineExerciseService.createRoutineExercise(request);
        return Response.created(URI.create("/api/v1/routine-exercises/" + response.id()))
                .entity(ApiResponse.success(201, "Ejercicio agregado a la rutina exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdateRoutineExerciseRequest request) {
        RoutineExerciseResponse response = routineExerciseService.updateRoutineExercise(id, request);
        return Response.ok(ApiResponse.success(200, "Ejercicio de rutina actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response delete(@PathParam("id") Long id) {
        routineExerciseService.deleteRoutineExercise(id);
        return Response.noContent().build();
    }
}
