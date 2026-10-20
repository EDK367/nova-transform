package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.workout.ExerciseService;
import org.nova.web.dto.request.workout.CreateExerciseRequest;
import org.nova.web.dto.request.workout.UpdateExerciseRequest;
import org.nova.web.dto.response.workout.ExerciseResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/exercises")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ExerciseResource {

    private final ExerciseService exerciseService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("muscleGroup") String muscleGroup) {
        List<ExerciseResponse> exercises = exerciseService.findAllResponses(muscleGroup);
        return Response.ok(ApiResponse.success(200, "Ejercicios obtenidos exitosamente", exercises)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        ExerciseResponse exercise = exerciseService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Ejercicio obtenido exitosamente", exercise)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response create(@Valid CreateExerciseRequest request) {
        ExerciseResponse response = exerciseService.createExercise(request);
        return Response.created(URI.create("/api/v1/exercises/" + response.id()))
                .entity(ApiResponse.success(201, "Ejercicio registrado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdateExerciseRequest request) {
        ExerciseResponse response = exerciseService.updateExercise(id, request);
        return Response.ok(ApiResponse.success(200, "Ejercicio actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        exerciseService.deleteExercise(id);
        return Response.noContent().build();
    }
}
