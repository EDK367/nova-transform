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
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.member.MemberGoalService;
import org.nova.web.dto.request.member.AssignMemberGoalRequest;
import org.nova.web.dto.response.member.MemberGoalResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/member-goals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MemberGoalResource {

    private final MemberGoalService memberGoalService;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("memberId") UUID memberId, @Context SecurityContext ctx) {
        UUID effectiveMemberId = memberId;
        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("ADMIN")
                && !ctx.isUserInRole("ENTRENADOR") && !ctx.isUserInRole("RECEPCIONISTA")) {
            String username = ctx.getUserPrincipal().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
            effectiveMemberId = user.getId();
        }
        List<MemberGoalResponse> list = memberGoalService.findAllResponses(effectiveMemberId);
        return Response.ok(ApiResponse.success(200, "Objetivos de socio obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        MemberGoalResponse response = memberGoalService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Objetivo de socio obtenido exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR", "CLIENTE"})
    public Response create(@Valid AssignMemberGoalRequest request) {
        MemberGoalResponse response = memberGoalService.assignGoal(request);
        return Response.created(URI.create("/api/v1/member-goals/" + response.id()))
                .entity(ApiResponse.success(201, "Objetivo asignado al socio exitosamente", response))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR", "CLIENTE"})
    public Response delete(@PathParam("id") Long id) {
        memberGoalService.deleteMemberGoal(id);
        return Response.noContent().build();
    }
}
