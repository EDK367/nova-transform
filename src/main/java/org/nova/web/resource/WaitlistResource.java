package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.domain.model.user.User;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.gymclass.WaitlistService;
import org.nova.web.dto.response.gymclass.WaitlistResponse;

import java.util.List;

@Path("/api/v1/waitlists")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class WaitlistResource {

    private final WaitlistService waitlistService;
    private final UserRepository userRepository;

    @GET
    @Path("/my")
    @RolesAllowed("CLIENTE")
    public Response getMyWaitlist(@Context SecurityContext ctx) {
        User user = getAuthenticatedUser(ctx);
        List<WaitlistResponse> list = waitlistService.findByMemberId(user.getId());
        return Response.ok(ApiResponse.success(200, "Mis registros en lista de espera obtenidos exitosamente", list)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response remove(@PathParam("id") Long id, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        waitlistService.removeFromWaitlist(id, username);
        return Response.ok(ApiResponse.success(200, "Retirado de la lista de espera exitosamente", null)).build();
    }

    private User getAuthenticatedUser(SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
    }
}
