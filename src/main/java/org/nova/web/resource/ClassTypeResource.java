package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.gymclass.ClassTypeService;
import org.nova.web.dto.request.gymclass.CreateClassTypeRequest;
import org.nova.web.dto.request.gymclass.UpdateClassTypeRequest;
import org.nova.web.dto.response.gymclass.ClassTypeResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/class-types")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ClassTypeResource {

    private final ClassTypeService classTypeService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<ClassTypeResponse> list = classTypeService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Tipos de clase obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        ClassTypeResponse response = classTypeService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Tipo de clase obtenido exitosamente", response)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreateClassTypeRequest request) {
        ClassTypeResponse response = classTypeService.create(request);
        return Response.created(URI.create("/api/v1/class-types/" + response.id()))
                .entity(ApiResponse.success(201, "Tipo de clase creado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdateClassTypeRequest request) {
        ClassTypeResponse response = classTypeService.update(id, request);
        return Response.ok(ApiResponse.success(200, "Tipo de clase actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        classTypeService.delete(id);
        return Response.ok(ApiResponse.success(200, "Tipo de clase eliminado exitosamente", null)).build();
    }
}
