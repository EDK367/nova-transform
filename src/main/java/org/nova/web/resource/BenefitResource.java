package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.BenefitService;
import org.nova.web.dto.request.membership.CreateBenefitRequest;
import org.nova.web.dto.request.membership.UpdateBenefitRequest;
import org.nova.web.dto.response.membership.BenefitResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/benefits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BenefitResource {

    private final BenefitService benefitService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<BenefitResponse> benefits = benefitService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Beneficios obtenidos exitosamente", benefits)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        BenefitResponse benefit = benefitService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Beneficio obtenido exitosamente", benefit)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateBenefitRequest request) {
        BenefitResponse response = benefitService.createBenefit(request);
        return Response.created(URI.create("/api/v1/benefits/" + response.id()))
                .entity(ApiResponse.success(201, "Beneficio creado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdateBenefitRequest request) {
        BenefitResponse response = benefitService.updateBenefit(id, request);
        return Response.ok(ApiResponse.success(200, "Beneficio actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        benefitService.deleteBenefit(id);
        return Response.noContent().build();
    }
}
