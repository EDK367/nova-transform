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
import org.nova.common.PageResponse;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.gymclass.AttendanceService;
import org.nova.web.dto.request.gymclass.CreateAttendanceRequest;
import org.nova.web.dto.request.gymclass.UpdateAttendanceExitRequest;
import org.nova.web.dto.response.gymclass.AttendanceResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/attendances")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class AttendanceResource {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("branchId") Long branchId,
            @QueryParam("memberId") UUID memberId,
            @QueryParam("date") LocalDate date,
            @QueryParam("sortBy") @DefaultValue("date") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("desc") String sortDirection
    ) {
        PageResponse<AttendanceResponse> paged = attendanceService.findPaged(
                page, size, branchId, memberId, date, sortBy, sortDirection
        );
        return Response.ok(ApiResponse.success(200, "Asistencias obtenidas exitosamente", paged)).build();
    }

    @GET
    @Path("/my")
    @RolesAllowed("CLIENTE")
    public Response getMyAttendances(@Context SecurityContext ctx) {
        User user = getAuthenticatedUser(ctx);
        List<AttendanceResponse> list = attendanceService.findByMemberId(user.getId());
        return Response.ok(ApiResponse.success(200, "Mis asistencias obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id, @Context SecurityContext ctx) {
        AttendanceResponse response = attendanceService.findResponseById(id);
        validateOwnershipIfClient(ctx, response.memberId());
        return Response.ok(ApiResponse.success(200, "Asistencia obtenida exitosamente", response)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response checkIn(@Valid CreateAttendanceRequest request, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        AttendanceResponse response = attendanceService.checkIn(request, username);
        return Response.created(URI.create("/api/v1/attendances/" + response.id()))
                .entity(ApiResponse.success(201, "Entrada registrada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}/checkout")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response checkOut(@PathParam("id") Long id, UpdateAttendanceExitRequest request) {
        AttendanceResponse response = attendanceService.checkOut(id, request);
        return Response.ok(ApiResponse.success(200, "Salida registrada exitosamente", response)).build();
    }

    private User getAuthenticatedUser(SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
    }

    private void validateOwnershipIfClient(SecurityContext ctx, UUID memberId) {
        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("ADMIN") && !ctx.isUserInRole("RECEPCIONISTA")) {
            User user = getAuthenticatedUser(ctx);
            if (!user.getId().equals(memberId)) {
                throw new AuthorizationException("No tiene permisos para acceder a este registro de asistencia");
            }
        }
    }
}
