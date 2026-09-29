package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.common.PageResponse;
import org.nova.service.membership.MembershipPlanService;
import org.nova.web.dto.request.membership.CreateMembershipPlanRequest;
import org.nova.web.dto.request.membership.UpdateMembershipPlanRequest;
import org.nova.web.dto.response.membership.BenefitResponse;
import org.nova.web.dto.response.membership.MembershipPlanResponse;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/membership-plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MembershipPlanResource {

    private final MembershipPlanService planService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sortBy") @DefaultValue("price") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDirection
    ) {
        if (page != null && size != null) {
            PageResponse<MembershipPlanResponse> paged = planService.findPaged(page, size, sortBy, sortDirection);
            return Response.ok(ApiResponse.success(200, "Planes obtenidos exitosamente", paged)).build();
        }
        List<MembershipPlanResponse> plans = planService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Planes obtenidos exitosamente", plans)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        MembershipPlanResponse plan = planService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Plan obtenido exitosamente", plan)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateMembershipPlanRequest request) {
        MembershipPlanResponse response = planService.createPlan(request);
        return Response.created(URI.create("/api/v1/membership-plans/" + response.id()))
                .entity(ApiResponse.success(201, "Plan de membresía creado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdateMembershipPlanRequest request) {
        MembershipPlanResponse response = planService.updatePlan(id, request);
        return Response.ok(ApiResponse.success(200, "Plan de membresía actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        planService.deletePlan(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/benefits/{benefitId}")
    @RolesAllowed("ADMIN")
    public Response addBenefit(@PathParam("id") Long id, @PathParam("benefitId") Long benefitId) {
        planService.addBenefitToPlan(id, benefitId);
        return Response.ok(ApiResponse.success(200, "Beneficio agregado al plan exitosamente", null)).build();
    }

    @DELETE
    @Path("/{id}/benefits/{benefitId}")
    @RolesAllowed("ADMIN")
    public Response removeBenefit(@PathParam("id") Long id, @PathParam("benefitId") Long benefitId) {
        planService.removeBenefitFromPlan(id, benefitId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/benefits")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getBenefits(@PathParam("id") Long id) {
        List<BenefitResponse> benefits = planService.getPlanBenefits(id);
        return Response.ok(ApiResponse.success(200, "Beneficios del plan obtenidos exitosamente", benefits)).build();
    }

    @GET
    @Path("/{id}/price-history")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getPriceHistory(@PathParam("id") Long id) {
        List<PlanPriceHistoryResponse> history = planService.getPriceHistories(id);
        return Response.ok(ApiResponse.success(200, "Historial de precios obtenido exitosamente", history)).build();
    }
}
