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
import org.nova.common.PageResponse;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.repository.UserRepository;
import org.nova.service.payment.PaymentReceiptService;
import org.nova.web.dto.request.payment.CreatePaymentReceiptRequest;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;

import java.net.URI;

@Path("/api/v1/payment-receipts")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PaymentReceiptResource {

    private final PaymentReceiptService paymentReceiptService;
    private final PaymentReceiptRepository paymentReceiptRepository;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("invoiceId") Long invoiceId,
            @QueryParam("sortBy") @DefaultValue("paymentDate") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("desc") String sortDirection
    ) {
        PageResponse<PaymentReceiptResponse> paged = paymentReceiptService.findPaged(
                page, size, invoiceId, sortBy, sortDirection
        );
        return Response.ok(ApiResponse.success(200, "Comprobantes de pago obtenidos exitosamente", paged)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getById(@PathParam("id") Long id, @Context SecurityContext ctx) {
        PaymentReceipt receipt = paymentReceiptRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Comprobante de pago no encontrado con ID: " + id));

        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("ADMIN") && !ctx.isUserInRole("RECEPCIONISTA")) {
            User user = getAuthenticatedUser(ctx);
            if (receipt.getIdInvoice() == null || receipt.getIdInvoice().getIdMembership() == null
                    || !user.getId().equals(receipt.getIdInvoice().getIdMembership().getIdMember().getId())) {
                throw new AuthorizationException("No tiene permisos para acceder a este comprobante");
            }
        }

        PaymentReceiptResponse response = paymentReceiptService.findResponseById(id);
        return Response.ok(ApiResponse.success(200, "Comprobante de pago obtenido exitosamente", response)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreatePaymentReceiptRequest request) {
        PaymentReceiptResponse response = paymentReceiptService.createReceipt(request);
        return Response.created(URI.create("/api/v1/payment-receipts/" + response.id()))
                .entity(ApiResponse.success(201, "Comprobante de pago registrado exitosamente", response))
                .build();
    }

    private User getAuthenticatedUser(SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
    }
}
