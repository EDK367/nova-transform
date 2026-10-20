package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.member.PhysicalMeasurementService;
import org.nova.web.dto.request.member.CreatePhysicalMeasurementRequest;
import org.nova.web.dto.request.member.UpdatePhysicalMeasurementRequest;
import org.nova.web.dto.response.member.PhysicalMeasurementResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/physical-measurements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PhysicalMeasurementResource {

    private final PhysicalMeasurementService physicalMeasurementService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll(@QueryParam("memberId") UUID memberId) {
        List<PhysicalMeasurementResponse> list = physicalMeasurementService.findAllResponses(memberId);
        return Response.ok(ApiResponse.success(200, "Mediciones fisicas obtenidas exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        PhysicalMeasurementResponse response = physicalMeasurementService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Medicion fisica obtenida exitosamente", response)).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response create(@Valid CreatePhysicalMeasurementRequest request) {
        PhysicalMeasurementResponse response = physicalMeasurementService.createMeasurement(request);
        return Response.created(URI.create("/api/v1/physical-measurements/" + response.id()))
                .entity(ApiResponse.success(201, "Medicion fisica registrada exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response update(@PathParam("id") Long id, @Valid UpdatePhysicalMeasurementRequest request) {
        PhysicalMeasurementResponse response = physicalMeasurementService.updateMeasurement(id, request);
        return Response.ok(ApiResponse.success(200, "Medicion fisica actualizada exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ENTRENADOR"})
    public Response delete(@PathParam("id") Long id) {
        physicalMeasurementService.deleteMeasurement(id);
        return Response.noContent().build();
    }
}
