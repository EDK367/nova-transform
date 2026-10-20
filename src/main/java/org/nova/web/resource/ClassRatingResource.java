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
import org.nova.service.gymclass.ClassRatingService;
import org.nova.web.dto.request.gymclass.CreateClassRatingRequest;
import org.nova.web.dto.response.gymclass.ClassRatingResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/class-ratings")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ClassRatingResource {

    private final ClassRatingService ratingService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        ClassRatingResponse response = ratingService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Calificación obtenida exitosamente", response)).build();
    }

    @GET
    @Path("/class/{classId}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getByClassId(@PathParam("classId") Long classId) {
        List<ClassRatingResponse> list = ratingService.findByClassId(classId);
        return Response.ok(ApiResponse.success(200, "Calificaciones obtenidas exitosamente", list)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("CLIENTE")
    public Response create(@Valid CreateClassRatingRequest request, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        ClassRatingResponse response = ratingService.createRating(request, username);
        return Response.created(URI.create("/api/v1/class-ratings/" + response.id()))
                .entity(ApiResponse.success(201, "Calificación registrada exitosamente", response))
                .build();
    }
}
