package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.common.PageResponse;
import org.nova.domain.enums.ClassStatus;
import org.nova.service.gymclass.ClassEnrollmentService;
import org.nova.service.gymclass.ClassRatingService;
import org.nova.service.gymclass.ClassService;
import org.nova.service.gymclass.WaitlistService;
import org.nova.web.dto.request.gymclass.CreateClassRequest;
import org.nova.web.dto.request.gymclass.UpdateClassRequest;
import org.nova.web.dto.response.gymclass.ClassEnrollmentResponse;
import org.nova.web.dto.response.gymclass.ClassRatingResponse;
import org.nova.web.dto.response.gymclass.ClassResponse;
import org.nova.web.dto.response.gymclass.WaitlistResponse;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/classes")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ClassResource {

    private final ClassService classService;
    private final ClassEnrollmentService enrollmentService;
    private final WaitlistService waitlistService;
    private final ClassRatingService ratingService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("branchId") Long branchId,
            @QueryParam("classTypeId") Long classTypeId,
            @QueryParam("trainerId") UUID trainerId,
            @QueryParam("status") ClassStatus status,
            @QueryParam("startDate") Instant startDate,
            @QueryParam("endDate") Instant endDate,
            @QueryParam("sortBy") @DefaultValue("schedule") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDirection
    ) {
        PageResponse<ClassResponse> paged = classService.findPaged(
                page, size, branchId, classTypeId, trainerId, status, startDate, endDate, sortBy, sortDirection
        );
        return Response.ok(ApiResponse.success(200, "Clases obtenidas exitosamente", paged)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        ClassResponse response = classService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Clase obtenida exitosamente", response)).build();
    }

    @GET
    @Path("/{id}/enrollments")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR"})
    public Response getEnrollments(@PathParam("id") Long id) {
        List<ClassEnrollmentResponse> list = enrollmentService.findByClassId(id);
        return Response.ok(ApiResponse.success(200, "Inscripciones obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}/waitlist")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR"})
    public Response getWaitlist(@PathParam("id") Long id) {
        List<WaitlistResponse> list = waitlistService.findByClassId(id);
        return Response.ok(ApiResponse.success(200, "Lista de espera obtenida exitosamente", list)).build();
    }

    @GET
    @Path("/{id}/ratings")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getRatings(@PathParam("id") Long id) {
        List<ClassRatingResponse> list = ratingService.findByClassId(id);
        return Response.ok(ApiResponse.success(200, "Calificaciones obtenidas exitosamente", list)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreateClassRequest request) {
        ClassResponse response = classService.createClass(request);
        return Response.created(URI.create("/api/v1/classes/" + response.id()))
                .entity(ApiResponse.success(201, "Clase programada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response update(@PathParam("id") Long id, @Valid UpdateClassRequest request) {
        ClassResponse response = classService.updateClass(id, request);
        return Response.ok(ApiResponse.success(200, "Clase actualizada exitosamente", response)).build();
    }

    @POST
    @Path("/{id}/cancel")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response cancel(@PathParam("id") Long id) {
        ClassResponse response = classService.cancelClass(id);
        return Response.ok(ApiResponse.success(200, "Clase cancelada exitosamente", response)).build();
    }
}
