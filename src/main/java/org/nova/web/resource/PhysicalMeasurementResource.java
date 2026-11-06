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
        List<PhysicalMeasurementResponse> list = physicalMeasurementService.findAllResponses(effectiveMemberId);
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
