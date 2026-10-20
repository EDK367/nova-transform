package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.workout.WorkoutLogService;
import org.nova.web.dto.request.workout.CreateWorkoutLogRequest;
import org.nova.web.dto.request.workout.UpdateWorkoutLogRequest;
import org.nova.web.dto.response.workout.WorkoutLogResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/workout-logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class WorkoutLogResource {

    private final WorkoutLogService workoutLogService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("memberId") UUID memberId,
            @QueryParam("routineExerciseId") Long routineExerciseId
    ) {
        List<WorkoutLogResponse> list = workoutLogService.findAllResponses(memberId, routineExerciseId);
        return Response.ok(ApiResponse.success(200, "Registros de entrenamiento obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        WorkoutLogResponse response = workoutLogService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Registro de entrenamiento obtenido exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR", "CLIENTE"})
    public Response create(@Valid CreateWorkoutLogRequest request) {
        WorkoutLogResponse response = workoutLogService.createWorkoutLog(request);
        return Response.created(URI.create("/api/v1/workout-logs/" + response.id()))
                .entity(ApiResponse.success(201, "Registro de entrenamiento guardado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR", "CLIENTE"})
    public Response update(@PathParam("id") Long id, @Valid UpdateWorkoutLogRequest request) {
        WorkoutLogResponse response = workoutLogService.updateWorkoutLog(id, request);
        return Response.ok(ApiResponse.success(200, "Registro de entrenamiento actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response delete(@PathParam("id") Long id) {
        workoutLogService.deleteWorkoutLog(id);
        return Response.noContent().build();
    }
}
