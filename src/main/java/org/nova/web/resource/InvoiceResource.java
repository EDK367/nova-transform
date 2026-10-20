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
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.service.payment.InvoiceService;
import org.nova.service.payment.PaymentReceiptService;
import org.nova.service.report.PdfReportService;
import org.nova.web.dto.request.payment.CreateInvoiceRequest;
import org.nova.web.dto.response.payment.InvoiceResponse;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/invoices")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class InvoiceResource {

    private final InvoiceService invoiceService;
    private final PaymentReceiptService paymentReceiptService;
    private final PdfReportService pdfReportService;
    private final UserRepository userRepository;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("status") InvoiceStatus status,
            @QueryParam("branchId") Long branchId,
            @QueryParam("memberId") UUID memberId,
            @QueryParam("sortBy") @DefaultValue("createdAt") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("desc") String sortDirection
    ) {
        PageResponse<InvoiceResponse> paged = invoiceService.findPaged(
                page, size, status, branchId, memberId, sortBy, sortDirection
        );
        return Response.ok(ApiResponse.success(200, "Facturas obtenidas exitosamente", paged)).build();
    }

    @GET
    @Path("/my")
    @RolesAllowed("CLIENTE")
    public Response getMyInvoices(@Context SecurityContext ctx) {
        User user = getAuthenticatedUser(ctx);
        List<InvoiceResponse> myInvoices = invoiceService.findByMemberId(user.getId());
        return Response.ok(ApiResponse.success(200, "Mis facturas obtenidas exitosamente", myInvoices)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getById(@PathParam("id") Long id, @Context SecurityContext ctx) {
        InvoiceResponse invoice = invoiceService.findResponseById(id);
        validateOwnershipIfClient(ctx, invoice.memberId());
        return Response.ok(ApiResponse.success(200, "Factura obtenida exitosamente", invoice)).build();
    }

    @GET
    @Path("/{id}/payments")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getInvoicePayments(@PathParam("id") Long id, @Context SecurityContext ctx) {
        InvoiceResponse invoice = invoiceService.findResponseById(id);
        validateOwnershipIfClient(ctx, invoice.memberId());
        List<PaymentReceiptResponse> receipts = paymentReceiptService.findByInvoiceId(id);
        return Response.ok(ApiResponse.success(200, "Comprobantes de pago obtenidos exitosamente", receipts)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreateInvoiceRequest request, @Context SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        InvoiceResponse response = invoiceService.createInvoice(request, username);
        return Response.created(URI.create("/api/v1/invoices/" + response.id()))
                .entity(ApiResponse.success(201, "Factura creada exitosamente", response))
                .build();
    }

    @POST
    @Path("/{id}/cancel")
    @RolesAllowed("ADMIN")
    public Response cancel(@PathParam("id") Long id) {
        InvoiceResponse response = invoiceService.cancelInvoice(id);
        return Response.ok(ApiResponse.success(200, "Factura cancelada exitosamente", response)).build();
    }

    @GET
    @Path("/{id}/pdf")
    @Produces("application/pdf")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response downloadPdf(@PathParam("id") Long id, @Context SecurityContext ctx) {
        InvoiceResponse invoice = invoiceService.findResponseById(id);
        validateOwnershipIfClient(ctx, invoice.memberId());
        byte[] pdfBytes = pdfReportService.generateInvoicePdf(id);
        return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"factura_" + id + ".pdf\"")
                .header("Content-Length", pdfBytes.length)
                .build();
    }

    private User getAuthenticatedUser(SecurityContext ctx) {
        String username = ctx.getUserPrincipal().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + username));
    }

    private void validateOwnershipIfClient(SecurityContext ctx, UUID memberId) {
        if (ctx.isUserInRole("CLIENTE") && !ctx.isUserInRole("ADMIN") && !ctx.isUserInRole("RECEPCIONISTA")) {
            User user = getAuthenticatedUser(ctx);
            if (!user.getId().equals(memberId)) {
                throw new AuthorizationException("No tiene permisos para acceder a esta factura");
            }
        }
    }
}
