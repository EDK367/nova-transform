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
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.membership.MembershipService;
import org.nova.web.dto.request.membership.CancelMembershipRequest;
import org.nova.web.dto.request.membership.ChangePlanRequest;
import org.nova.web.dto.request.membership.CreateMembershipRequest;
import org.nova.web.dto.request.membership.FreezeMembershipRequest;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;
import org.nova.web.dto.response.membership.MembershipResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/memberships")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MembershipResource {

    private final MembershipService membershipService;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("status") MembershipStatus status,
            @QueryParam("memberId") UUID memberId,
            @QueryParam("sortBy") @DefaultValue("createdAt") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("desc") String sortDirection
    ) {
        PageResponse<MembershipResponse> paged = membershipService.findPaged(
                page, size, status, memberId, sortBy, sortDirection
        );
        return Response.ok(ApiResponse.success(200, "Membresías obtenidas exitosamente", paged)).build();
    }

    @GET
    @Path("/my")
    @RolesAllowed("CLIENTE")
    public Response getMyMemberships(@Context SecurityContext ctx) {
        User user = getAuthenticatedUser(ctx);
        List<MembershipResponse> myMemberships = membershipService.findByMemberId(user.getId());
        return Response.ok(ApiResponse.success(200, "Mis membresías obtenidas exitosamente", myMemberships)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getById(@PathParam("id") Long id, @Context SecurityContext ctx) {
        MembershipResponse membership = membershipService.findResponseById(id);
        validateOwnershipIfClient(ctx, membership.memberId());
        return Response.ok(ApiResponse.success(200, "Membresía obtenida exitosamente", membership)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response subscribe(@Valid CreateMembershipRequest request) {
        MembershipResponse response = membershipService.subscribeMember(request);
        return Response.created(URI.create("/api/v1/memberships/" + response.id()))
                .entity(ApiResponse.success(201, "Socio suscrito a la membresía exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}/change-plan")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"RECEPCIONISTA", "ADMIN"})
    public Response changePlan(@PathParam("id") Long id, @Valid ChangePlanRequest request) {
        MembershipResponse response = membershipService.changePlan(id, request);
        return Response.ok(ApiResponse.success(200, "Plan de la membresía actualizado exitosamente", response)).build();
    }

    @POST
    @Path("/{id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response cancel(@PathParam("id") Long id, CancelMembershipRequest request) {
        MembershipResponse response = membershipService.cancelMembership(id, request);
        return Response.ok(ApiResponse.success(200, "Suscripción cancelada exitosamente por administrador", response)).build();
    }

    @POST
    @Path("/{id}/freeze")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response freeze(@PathParam("id") Long id, @Valid FreezeMembershipRequest request) {
        MembershipResponse response = membershipService.freezeMembership(id, request);
        return Response.ok(ApiResponse.success(200, "Membresía congelada exitosamente", response)).build();
    }

    @POST
    @Path("/{id}/unfreeze")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response unfreeze(@PathParam("id") Long id) {
        MembershipResponse response = membershipService.unfreezeMembership(id);
        return Response.ok(ApiResponse.success(200, "Membresía descongelada y reactivada exitosamente", response)).build();
    }

    @GET
    @Path("/{id}/history")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getHistory(@PathParam("id") Long id, @Context SecurityContext ctx) {
        MembershipResponse membership = membershipService.findResponseById(id);
        validateOwnershipIfClient(ctx, membership.memberId());
        List<MembershipHistoryResponse> history = membershipService.getMembershipHistory(id);
        return Response.ok(ApiResponse.success(200, "Historial de membresía obtenido exitosamente", history)).build();
    }

    @GET
    @Path("/{id}/freezes")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getFreezes(@PathParam("id") Long id, @Context SecurityContext ctx) {
        MembershipResponse membership = membershipService.findResponseById(id);
        validateOwnershipIfClient(ctx, membership.memberId());
        List<MembershipFreezeResponse> freezes = membershipService.getMembershipFreezes(id);
        return Response.ok(ApiResponse.success(200, "Congelamientos de la membresía obtenidos exitosamente", freezes)).build();
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
                throw new AuthorizationException("No tiene permisos para acceder a esta membresía");
            }
        }
    }
}
