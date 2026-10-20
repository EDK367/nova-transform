package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.MembershipFreezeService;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;

import java.util.List;

@Path("/api/v1/membership-freezes")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MembershipFreezeResource {

    private final MembershipFreezeService membershipFreezeService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getAll(@QueryParam("membershipId") Long membershipId) {
        List<MembershipFreezeResponse> list = membershipFreezeService.findAllResponses(membershipId);
        return Response.ok(ApiResponse.success(200, "Congelamientos de membresia obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        MembershipFreezeResponse response = membershipFreezeService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Congelamiento obtenido exitosamente", response)).build();
    }
}
