package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.member.GoalService;
import org.nova.web.dto.request.member.CreateGoalRequest;
import org.nova.web.dto.request.member.UpdateGoalRequest;
import org.nova.web.dto.response.member.GoalResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/goals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class GoalResource {

    private final GoalService goalService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<GoalResponse> goals = goalService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Objetivos obtenidos exitosamente", goals)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        GoalResponse goal = goalService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Objetivo obtenido exitosamente", goal)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response create(@Valid CreateGoalRequest request) {
        GoalResponse response = goalService.createGoal(request);
        return Response.created(URI.create("/api/v1/goals/" + response.id()))
                .entity(ApiResponse.success(201, "Objetivo registrado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdateGoalRequest request) {
        GoalResponse response = goalService.updateGoal(id, request);
        return Response.ok(ApiResponse.success(200, "Objetivo actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        goalService.deleteGoal(id);
        return Response.noContent().build();
    }
}
