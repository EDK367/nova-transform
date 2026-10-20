package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.service.report.PdfReportService;

import java.util.UUID;

@Path("/api/v1/reports")
@RequiredArgsConstructor
public class ReportResource {

    private final PdfReportService pdfReportService;

    @GET
    @Path("/invoices/{id}/pdf")
    @Produces("application/pdf")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response getInvoicePdf(@PathParam("id") Long id) {
        byte[] pdf = pdfReportService.generateInvoicePdf(id);
        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=\"factura_" + id + ".pdf\"")
                .header("Content-Length", pdf.length)
                .build();
    }

    @GET
    @Path("/members/{id}/carnet-pdf")
    @Produces("application/pdf")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getCarnetPdf(@PathParam("id") UUID id) {
        byte[] pdf = pdfReportService.generateMemberCarnetPdf(id);
        return Response.ok(pdf)
                .header("Content-Disposition", "inline; filename=\"carnet_socio_" + id + ".pdf\"")
                .header("Content-Length", pdf.length)
                .build();
    }

    @GET
    @Path("/attendances/pdf")
    @Produces("application/pdf")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA", "ENTRENADOR"})
    public Response getAttendanceReportPdf(
            @QueryParam("memberId") UUID memberId,
            @QueryParam("branchId") Long branchId
    ) {
        byte[] pdf = pdfReportService.generateAttendanceReportPdf(memberId, branchId);
        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=\"reporte_asistencias.pdf\"")
                .header("Content-Length", pdf.length)
                .build();
    }
}
