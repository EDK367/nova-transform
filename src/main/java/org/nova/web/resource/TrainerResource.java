package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.trainer.TrainerService;
import org.nova.web.dto.request.trainer.CreateTrainerRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerRequest;
import org.nova.web.dto.response.trainer.TrainerResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TrainerResource {

    private final TrainerService trainerService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<TrainerResponse> trainers = trainerService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Entrenadores obtenidos exitosamente", trainers)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") UUID id) {
        TrainerResponse trainer = trainerService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Entrenador obtenido exitosamente", trainer)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateTrainerRequest request) {
        TrainerResponse response = trainerService.createTrainer(request);
        return Response.created(URI.create("/api/v1/trainers/" + response.id()))
                .entity(ApiResponse.success(201, "Entrenador registrado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") UUID id, @Valid UpdateTrainerRequest request) {
        TrainerResponse response = trainerService.updateTrainer(id, request);
        return Response.ok(ApiResponse.success(200, "Entrenador actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") UUID id) {
        trainerService.deleteTrainer(id);
        return Response.noContent().build();
    }
}
