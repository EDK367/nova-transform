package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.staff.ReceptionistService;
import org.nova.web.dto.request.staff.CreateReceptionistRequest;
import org.nova.web.dto.request.staff.UpdateReceptionistRequest;
import org.nova.web.dto.response.staff.ReceptionistResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/receptionists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@RolesAllowed("ADMIN")
public class ReceptionistResource {

    private final ReceptionistService receptionistService;

    @GET
    public Response getAll() {
        List<ReceptionistResponse> receptionists = receptionistService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Recepcionistas obtenidos exitosamente", receptionists)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        ReceptionistResponse receptionist = receptionistService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Recepcionista obtenido exitosamente", receptionist)).build();
    }

    @POST
    public Response create(@Valid CreateReceptionistRequest request) {
        ReceptionistResponse response = receptionistService.createReceptionist(request);
        return Response.created(URI.create("/api/v1/receptionists/" + response.id()))
                .entity(ApiResponse.success(201, "Recepcionista registrado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid UpdateReceptionistRequest request) {
        ReceptionistResponse response = receptionistService.updateReceptionist(id, request);
        return Response.ok(ApiResponse.success(200, "Recepcionista actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        receptionistService.deleteReceptionist(id);
        return Response.noContent().build();
    }
}
