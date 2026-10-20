package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.MembershipHistoryService;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;

import java.util.List;

@Path("/api/v1/membership-histories")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MembershipHistoryResource {

    private final MembershipHistoryService membershipHistoryService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getAll(@QueryParam("membershipId") Long membershipId) {
        List<MembershipHistoryResponse> list = membershipHistoryService.findAllResponses(membershipId);
        return Response.ok(ApiResponse.success(200, "Historiales de membresia obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        MembershipHistoryResponse response = membershipHistoryService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Historial obtenido exitosamente", response)).build();
    }
}
