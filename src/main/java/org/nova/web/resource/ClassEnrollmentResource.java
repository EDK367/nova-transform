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
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.gymclass.ClassEnrollmentService;
import org.nova.web.dto.request.gymclass.CreateEnrollmentRequest;
import org.nova.web.dto.response.gymclass.ClassEnrollmentResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/class-enrollments")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ClassEnrollmentResource {

    private final ClassEnrollmentService enrollmentService;
    private final UserRepository userRepository;

    @GET
    @Path("/my")
    @RolesAllowed("CLIENTE")
    public Response getMyEnrollments(@Context SecurityContext ctx) {
        User user = getAuthenticatedUser(ctx);
        List<ClassEnrollmentResponse> list = enrollmentService.findByMemberId(user.getId());
        return Response.ok(ApiResponse.success(200, "Mis inscripciones obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id, @Context SecurityContext ctx) {
        ClassEnrollmentResponse response = enrollmentService.findResponseById(id);
        validateOwnershipIfClient(ctx, response.memberId());
        return Response.ok(ApiResponse.success(200, "Inscripción obtenida exitosamente", response)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response enroll(@Valid CreateEnrollmentRequest request, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        ClassEnrollmentResponse response = enrollmentService.enroll(request, username);
        return Response.created(URI.create("/api/v1/class-enrollments/" + response.id()))
                .entity(ApiResponse.success(201, "Inscripción realizada exitosamente", response))
                .build();
    }

    @POST
    @Path("/{id}/cancel")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response cancel(@PathParam("id") Long id, @Context SecurityContext ctx) {
        ClassEnrollmentResponse existing = enrollmentService.findResponseById(id);
        validateOwnershipIfClient(ctx, existing.memberId());
        String username = ctx.getUserPrincipal().getName();
        ClassEnrollmentResponse response = enrollmentService.cancelEnrollment(id, username);
        return Response.ok(ApiResponse.success(200, "Inscripción cancelada exitosamente", response)).build();
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
                throw new AuthorizationException("No tiene permisos para acceder a esta inscripción");
            }
        }
    }
}
