package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.PlanPriceHistoryService;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;

import java.util.List;

@Path("/api/v1/plan-price-histories")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PlanPriceHistoryResource {

    private final PlanPriceHistoryService planPriceHistoryService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("planId") Long planId) {
        List<PlanPriceHistoryResponse> list = planPriceHistoryService.findAllResponses(planId);
        return Response.ok(ApiResponse.success(200, "Historiales de precios obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        PlanPriceHistoryResponse response = planPriceHistoryService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Historial de precio obtenido exitosamente", response)).build();
    }
}
