package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.service.payment.PaymentMethodService;
import org.nova.web.dto.request.payment.CreatePaymentMethodRequest;
import org.nova.web.dto.request.payment.UpdatePaymentMethodRequest;
import org.nova.web.dto.response.payment.PaymentMethodResponse;

import java.net.URI;
import java.util.List;

@Path("/api/v1/payment-methods")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PaymentMethodResource {

    private final PaymentMethodService paymentMethodService;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getAll() {
        List<PaymentMethodResponse> list = paymentMethodService.findAllResponses();
        return Response.ok(ApiResponse.success(200, "Métodos de pago obtenidos exitosamente", list)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getById(@PathParam("id") Long id) {
        PaymentMethodResponse response = paymentMethodService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Método de pago obtenido exitosamente", response)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response create(@Valid CreatePaymentMethodRequest request) {
        PaymentMethodResponse response = paymentMethodService.create(request);
        return Response.created(URI.create("/api/v1/payment-methods/" + response.id()))
                .entity(ApiResponse.success(201, "Método de pago creado exitosamente", response))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid UpdatePaymentMethodRequest request) {
        PaymentMethodResponse response = paymentMethodService.update(id, request);
        return Response.ok(ApiResponse.success(200, "Método de pago actualizado exitosamente", response)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        paymentMethodService.delete(id);
        return Response.ok(ApiResponse.success(200, "Método de pago eliminado exitosamente", null)).build();
    }
}
