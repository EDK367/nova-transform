package org.nova.service.report;

import java.util.UUID;

public interface PdfReportService {

    byte[] generateInvoicePdf(Long invoiceId);

    byte[] generateMemberCarnetPdf(UUID memberId);

    byte[] generateAttendanceReportPdf(UUID memberId, Long branchId);
}
