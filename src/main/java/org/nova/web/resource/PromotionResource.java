package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.membership.PromotionService;
import org.nova.web.dto.request.membership.CreatePromotionRequest;
import org.nova.web.dto.request.membership.UpdatePromotionRequest;
import org.nova.web.dto.response.membership.PromotionResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PromotionResource {

    private final PromotionService promotionService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("onlyActive") @DefaultValue("false") boolean onlyActive) {
        List<PromotionResponse> promotions = promotionService.findAllResponses(onlyActive);
        return Response.ok(ApiResponse.success(200, "Promociones obtenidas exitosamente", promotions)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        PromotionResponse promotion = promotionService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Promoción obtenida exitosamente", promotion)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreatePromotionRequest request) {
        PromotionResponse response = promotionService.createPromotion(request);
        return Response.created(URI.create("/api/v1/promotions/" + response.id()))
                .entity(ApiResponse.success(201, "Promoción creada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response update(@PathParam("id") Long id, @Valid UpdatePromotionRequest request) {
        PromotionResponse response = promotionService.updatePromotion(id, request);
        return Response.ok(ApiResponse.success(200, "Promoción actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        promotionService.deletePromotion(id);
        return Response.noContent().build();
    }
}
