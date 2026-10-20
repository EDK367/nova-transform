package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.BenefitPlanService;
import org.nova.web.dto.request.membership.AssignBenefitPlanRequest;
import org.nova.web.dto.response.membership.BenefitPlanResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/benefit-plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BenefitPlanResource {

    private final BenefitPlanService benefitPlanService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("planId") Long planId) {
        List<BenefitPlanResponse> list = benefitPlanService.findAllResponses(planId);
        return Response.ok(ApiResponse.success(200, "Beneficios de planes obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        BenefitPlanResponse response = benefitPlanService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Beneficio de plan obtenido exitosamente", response)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid AssignBenefitPlanRequest request) {
        BenefitPlanResponse response = benefitPlanService.assignBenefitToPlan(request);
        return Response.created(URI.create("/api/v1/benefit-plans/" + response.id()))
                .entity(ApiResponse.success(201, "Beneficio asignado al plan exitosamente", response))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        benefitPlanService.deleteBenefitPlan(id);
        return Response.noContent().build();
    }
}
