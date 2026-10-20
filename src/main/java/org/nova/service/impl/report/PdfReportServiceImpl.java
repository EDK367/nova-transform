package org.nova.service.impl.report;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfShading;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.domain.model.user.User;
import org.nova.exception.NotFoundException;
import org.nova.repository.*;
import org.nova.service.report.PdfReportService;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class PdfReportServiceImpl implements PdfReportService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final AttendanceRepository attendanceRepository;
    private final BranchRepository branchRepository;

    @Override
    public byte[] generateInvoicePdf(Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdWithDetails(invoiceId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con ID: " + invoiceId));

        List<PaymentReceipt> receipts = paymentReceiptRepository.findByInvoiceId(invoiceId);
        BigDecimal totalPaid = paymentReceiptRepository.sumPaidByInvoiceId(invoiceId);
        BigDecimal remainingBalance = invoice.getAmount().subtract(totalPaid);

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE);
            Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(226, 232, 240));
            Font fontSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(15, 23, 42));
            Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(71, 85, 105));
            Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(15, 23, 42));
            Font fontAmount = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(22, 101, 52));
            Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new Color(100, 116, 139));

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(new Color(15, 23, 42));
            headerCell.setPadding(16);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Paragraph title = new Paragraph("NOVA TRANSFORM", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(title);

            Paragraph subtitle = new Paragraph("COMPROBANTE OFICIAL DE PAGO Y FACTURACION", fontSubtitle);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(subtitle);

            headerTable.addCell(headerCell);
            document.add(headerTable);
            document.add(new Paragraph(" "));

            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);
            metaTable.setWidths(new float[]{50f, 50f});

            addTableCell(metaTable, "No. Factura:", String.format("#%06d", invoice.getId()), fontLabel, fontValue);
            addTableCell(metaTable, "Estado:", invoice.getStatus() != null ? invoice.getStatus().name() : "N/A", fontLabel, fontValue);
            addTableCell(metaTable, "Fecha Emision:", invoice.getIssueDate() != null ? invoice.getIssueDate().toString() : "N/A", fontLabel, fontValue);
            addTableCell(metaTable, "Fecha Vencimiento:", invoice.getDueDate() != null ? invoice.getDueDate().toString() : "N/A", fontLabel, fontValue);
            addTableCell(metaTable, "Sucursal:", invoice.getIdBranch() != null ? invoice.getIdBranch().getName() : "N/A", fontLabel, fontValue);
            addTableCell(metaTable, "Recepcionista:", invoice.getIdReceptionist() != null && invoice.getIdReceptionist().getUsers() != null ? invoice.getIdReceptionist().getUsers().getFirstName() + " " + invoice.getIdReceptionist().getUsers().getLastName() : "N/A", fontLabel, fontValue);

            document.add(metaTable);
            document.add(new Paragraph(" "));

            document.add(createSectionTitle("DATOS DEL SOCIO", fontSection));
            PdfPTable memberTable = new PdfPTable(2);
            memberTable.setWidthPercentage(100);
            memberTable.setWidths(new float[]{30f, 70f});

            User memberUser = (invoice.getIdMembership() != null && invoice.getIdMembership().getIdMember() != null)
                    ? invoice.getIdMembership().getIdMember().getUsers()
                    : null;

            addTableCell(memberTable, "Nombre Completo:", memberUser != null ? memberUser.getFirstName() + " " + memberUser.getLastName() : "N/A", fontLabel, fontValue);
            addTableCell(memberTable, "Usuario:", memberUser != null ? memberUser.getUsername() : "N/A", fontLabel, fontValue);
            addTableCell(memberTable, "Correo:", memberUser != null ? memberUser.getEmail() : "N/A", fontLabel, fontValue);
            addTableCell(memberTable, "Telefono:", memberUser != null && memberUser.getPhone() != null ? memberUser.getPhone() : "N/A", fontLabel, fontValue);

            document.add(memberTable);
            document.add(new Paragraph(" "));

            document.add(createSectionTitle("DETALLES DE MEMBRESIA Y PLAN", fontSection));
            PdfPTable planTable = new PdfPTable(2);
            planTable.setWidthPercentage(100);
            planTable.setWidths(new float[]{40f, 60f});

            Membership membership = invoice.getIdMembership();
            addTableCell(planTable, "Plan Contratado:", (membership != null && membership.getIdPlan() != null) ? membership.getIdPlan().getName() : "N/A", fontLabel, fontValue);
            addTableCell(planTable, "Duracion:", (membership != null && membership.getIdPlan() != null) ? membership.getIdPlan().getDuration() + " dias" : "N/A", fontLabel, fontValue);
            addTableCell(planTable, "Fecha Inicio Membresia:", (membership != null && membership.getStartDate() != null) ? membership.getStartDate().toString() : "N/A", fontLabel, fontValue);
            addTableCell(planTable, "Fecha Fin Membresia:", (membership != null && membership.getDueDate() != null) ? membership.getDueDate().toString() : "N/A", fontLabel, fontValue);

            document.add(planTable);
            document.add(new Paragraph(" "));

            document.add(createSectionTitle("RESUMEN DE MONTOS", fontSection));
            PdfPTable amountTable = new PdfPTable(2);
            amountTable.setWidthPercentage(100);
            amountTable.setWidths(new float[]{50f, 50f});

            addTableCell(amountTable, "Monto Total Factura:", "Q" + invoice.getAmount(), fontLabel, fontAmount);
            addTableCell(amountTable, "Total Pagado:", "Q" + totalPaid, fontLabel, fontAmount);
            addTableCell(amountTable, "Saldo Pendiente:", "Q" + remainingBalance, fontLabel, fontValue);

            document.add(amountTable);
            document.add(new Paragraph(" "));

            if (!receipts.isEmpty()) {
                document.add(createSectionTitle("RECIBOS DE PAGO REGISTRADOS", fontSection));
                PdfPTable receiptTable = new PdfPTable(4);
                receiptTable.setWidthPercentage(100);
                receiptTable.setWidths(new float[]{25f, 30f, 25f, 20f});

                addHeaderCell(receiptTable, "Referencia", fontLabel);
                addHeaderCell(receiptTable, "Metodo de Pago", fontLabel);
                addHeaderCell(receiptTable, "Fecha", fontLabel);
                addHeaderCell(receiptTable, "Monto", fontLabel);

                for (PaymentReceipt r : receipts) {
                    addCell(receiptTable, r.getReference() != null ? r.getReference() : "N/A", fontValue);
                    addCell(receiptTable, r.getIdPaymentMethod() != null ? r.getIdPaymentMethod().getName() : "N/A", fontValue);
                    addCell(receiptTable, r.getPaymentDate() != null ? r.getPaymentDate().toString() : "N/A", fontValue);
                    addCell(receiptTable, "Q" + r.getAmountPaid(), fontValue);
                }
                document.add(receiptTable);
                document.add(new Paragraph(" "));
            }

            PdfPTable qrTable = new PdfPTable(2);
            qrTable.setWidthPercentage(100);
            qrTable.setWidths(new float[]{70f, 30f});

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.addElement(new Paragraph("Nova Transform - Sistema de Gestion Fitness", fontLabel));
            textCell.addElement(new Paragraph("Valida este comprobante escaneando el codigo QR oficial.", fontFooter));
            qrTable.addCell(textCell);

            String qrContent = "NOVA-INVOICE:" + invoice.getId() + ":STATUS:" + invoice.getStatus() + ":AMOUNT:" + invoice.getAmount();
            byte[] qrBytes = generateQrCodeBytes(qrContent, 120, 120);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell qrCell = new PdfPCell(qrImage);
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            qrTable.addCell(qrCell);

            document.add(qrTable);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de la factura: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] generateMemberCarnetPdf(UUID memberId) {
        Member member = memberRepository.findByIdWithUser(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));

        User user = member.getUsers();
        Membership activeMembership = membershipRepository.findActiveByMemberId(memberId).orElse(null);

        float cardWidth = 530f;
        float cardHeight = 330f;
        Rectangle cardSize = new Rectangle(cardWidth, cardHeight);
        Document document = new Document(cardSize, 0, 0, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            PdfContentByte cb = writer.getDirectContentUnder();

            cb.saveState();
            cb.setColorFill(new Color(15, 23, 42));
            cb.rectangle(0, 0, cardWidth, cardHeight);
            cb.fill();
            cb.restoreState();

            float headerHeight = 65f;
            PdfShading headerShading = PdfShading.simpleAxial(
                    writer,
                    0, cardHeight,
                    cardWidth, cardHeight - headerHeight,
                    new Color(4, 120, 87),
                    new Color(16, 185, 129)
            );
            cb.saveState();
            cb.rectangle(0, cardHeight - headerHeight, cardWidth, headerHeight);
            cb.clip();
            cb.newPath();
            cb.paintShading(headerShading);
            cb.restoreState();

            cb.saveState();
            cb.setColorFill(new Color(52, 211, 153));
            cb.rectangle(0, cardHeight - headerHeight - 2.5f, cardWidth, 2.5f);
            cb.fill();
            cb.restoreState();

            float footerHeight = 22f;
            PdfShading footerShading = PdfShading.simpleAxial(
                    writer,
                    0, 0,
                    cardWidth, footerHeight,
                    new Color(16, 185, 129),
                    new Color(4, 120, 87)
            );
            cb.saveState();
            cb.rectangle(0, 0, cardWidth, footerHeight);
            cb.clip();
            cb.newPath();
            cb.paintShading(footerShading);
            cb.restoreState();

            cb.saveState();
            cb.setColorFill(new Color(52, 211, 153));
            cb.rectangle(0, footerHeight, cardWidth, 1.5f);
            cb.fill();
            cb.restoreState();

            cb.saveState();
            cb.setColorStroke(new Color(16, 185, 129));
            cb.setLineWidth(1.5f);
            cb.rectangle(1, 1, cardWidth - 2, cardHeight - 2);
            cb.stroke();
            cb.restoreState();

            Font fontHeaderTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE);
            Font fontHeaderSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, new Color(209, 250, 229));
            Font fontBadge = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.WHITE);
            Font fontMemberName = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.WHITE);
            Font fontMemberSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, new Color(52, 211, 153));
            Font fontUuidLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7f, new Color(148, 163, 184));
            Font fontUuidValue = FontFactory.getFont(FontFactory.COURIER_BOLD, 7.5f, new Color(52, 211, 153));
            Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, new Color(148, 163, 184));
            Font fontValue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8f, new Color(248, 250, 252));
            Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7f, Color.WHITE);
            Font fontQrSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6.5f, new Color(148, 163, 184));

            PdfPTable master = new PdfPTable(1);
            master.setWidthPercentage(100);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{70f, 30f});

            PdfPCell leftHeader = new PdfPCell();
            leftHeader.setBorder(Rectangle.NO_BORDER);
            leftHeader.setPaddingLeft(18f);
            leftHeader.setPaddingTop(12f);
            leftHeader.setPaddingBottom(8f);
            Paragraph pTitle = new Paragraph("NOVA TRANSFORM", fontHeaderTitle);
            Paragraph pSub = new Paragraph("CREDENCIAL DIGITAL DE SOCIO • ACCESO GENERAL", fontHeaderSub);
            leftHeader.addElement(pTitle);
            leftHeader.addElement(pSub);
            headerTable.addCell(leftHeader);

            PdfPCell rightHeader = new PdfPCell();
            rightHeader.setBorder(Rectangle.NO_BORDER);
            rightHeader.setPaddingRight(18f);
            rightHeader.setPaddingTop(16f);
            rightHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell pillCell = new PdfPCell(new Phrase(activeMembership != null ? "● ACTIVO" : "● INACTIVO", fontBadge));
            pillCell.setBackgroundColor(activeMembership != null ? new Color(16, 185, 129) : new Color(239, 68, 68));
            pillCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pillCell.setPaddingTop(4f);
            pillCell.setPaddingBottom(4f);
            pillCell.setPaddingLeft(8f);
            pillCell.setPaddingRight(8f);
            pillCell.setBorder(Rectangle.NO_BORDER);
            PdfPTable pillTable = new PdfPTable(1);
            pillTable.setWidthPercentage(75f);
            pillTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pillTable.addCell(pillCell);
            rightHeader.addElement(pillTable);
            headerTable.addCell(rightHeader);

            PdfPCell hCell = new PdfPCell(headerTable);
            hCell.setBorder(Rectangle.NO_BORDER);
            master.addCell(hCell);

            PdfPTable bodyTable = new PdfPTable(2);
            bodyTable.setWidthPercentage(100);
            bodyTable.setWidths(new float[]{63f, 37f});

            PdfPCell leftBody = new PdfPCell();
            leftBody.setBorder(Rectangle.NO_BORDER);
            leftBody.setPaddingLeft(18f);
            leftBody.setPaddingRight(10f);
            leftBody.setPaddingTop(10f);

            Paragraph nameP = new Paragraph(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase(), fontMemberName);
            Paragraph subP = new Paragraph("SOCIO TITULAR REGISTRADO", fontMemberSub);
            leftBody.addElement(nameP);
            leftBody.addElement(subP);

            PdfPTable uuidBox = new PdfPTable(1);
            uuidBox.setWidthPercentage(100);
            uuidBox.setSpacingBefore(5f);
            uuidBox.setSpacingAfter(6f);
            PdfPCell uuidCell = new PdfPCell();
            uuidCell.setBackgroundColor(new Color(30, 41, 59));
            uuidCell.setBorderColor(new Color(16, 185, 129));
            uuidCell.setBorderWidth(1f);
            uuidCell.setPadding(4f);
            Paragraph uuidP = new Paragraph();
            uuidP.add(new Phrase("UUID: ", fontUuidLabel));
            uuidP.add(new Phrase(member.getId().toString(), fontUuidValue));
            uuidCell.addElement(uuidP);
            uuidBox.addCell(uuidCell);
            leftBody.addElement(uuidBox);

            PdfPTable details = new PdfPTable(2);
            details.setWidthPercentage(100);
            details.setWidths(new float[]{30f, 70f});

            addCarnetRow(details, "Usuario:", "@" + user.getUsername(), fontLabel, fontValue);
            addCarnetRow(details, "Correo:", user.getEmail(), fontLabel, fontValue);
            addCarnetRow(details, "Sede:", user.getBranch() != null ? user.getBranch().getName() : "Sede Principal", fontLabel, fontValue);
            addCarnetRow(details, "Plan:", activeMembership != null && activeMembership.getIdPlan() != null ? activeMembership.getIdPlan().getName() : "Sin Plan Activo", fontLabel, fontValue);
            addCarnetRow(details, "Vence:", activeMembership != null && activeMembership.getDueDate() != null ? activeMembership.getDueDate().toString() : "N/A", fontLabel, fontValue);

            leftBody.addElement(details);
            bodyTable.addCell(leftBody);

            PdfPCell rightBody = new PdfPCell();
            rightBody.setBorder(Rectangle.NO_BORDER);
            rightBody.setPaddingRight(18f);
            rightBody.setPaddingLeft(10f);
            rightBody.setPaddingTop(10f);
            rightBody.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPTable qrCard = new PdfPTable(1);
            qrCard.setWidthPercentage(95f);
            qrCard.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell qrInnerCell = new PdfPCell();
            qrInnerCell.setBackgroundColor(Color.WHITE);
            qrInnerCell.setBorderColor(new Color(16, 185, 129));
            qrInnerCell.setBorderWidth(1.5f);
            qrInnerCell.setPadding(6f);
            qrInnerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            String qrPayload = "NOVA-MEMBER-ID:" + member.getId() + ":NAME:" + user.getFirstName() + " " + user.getLastName() + ":PLAN:" + (activeMembership != null && activeMembership.getIdPlan() != null ? activeMembership.getIdPlan().getName() : "NONE");
            byte[] qrBytes = generateQrCodeBytes(qrPayload, 110, 110);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            qrInnerCell.addElement(qrImage);

            Paragraph qrSub = new Paragraph("CÓDIGO DE ACCESO", fontQrSub);
            qrSub.setAlignment(Element.ALIGN_CENTER);
            qrInnerCell.addElement(qrSub);

            qrCard.addCell(qrInnerCell);
            rightBody.addElement(qrCard);
            bodyTable.addCell(rightBody);

            PdfPCell bCell = new PdfPCell(bodyTable);
            bCell.setBorder(Rectangle.NO_BORDER);
            master.addCell(bCell);

            PdfPCell spacerCell = new PdfPCell();
            spacerCell.setBorder(Rectangle.NO_BORDER);
            spacerCell.setFixedHeight(12f);
            master.addCell(spacerCell);

            PdfPCell footerCell = new PdfPCell();
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph footP = new Paragraph("NOVA TRANSFORM FITNESS CLUB • IDENTIFICACIÓN OFICIAL INTRANSFERIBLE", fontFooter);
            footP.setAlignment(Element.ALIGN_CENTER);
            footerCell.addElement(footP);
            master.addCell(footerCell);

            document.add(master);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Carnet de socio: " + e.getMessage(), e);
        }
    }

    private void addCarnetRow(PdfPTable table, String label, String value, Font fLabel, Font fValue) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, fLabel));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPaddingTop(2f);
        c1.setPaddingBottom(2f);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(value, fValue));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPaddingTop(2f);
        c2.setPaddingBottom(2f);
        table.addCell(c2);
    }

    @Override
    public byte[] generateAttendanceReportPdf(UUID memberId, Long branchId) {
        List<Attendance> attendances;
        Member member = null;
        Branch branch = null;

        if (memberId != null) {
            member = memberRepository.findByIdWithUser(memberId).orElse(null);
            attendances = attendanceRepository.findByMemberId(memberId);
        } else if (branchId != null) {
            branch = branchRepository.findByIdOptional(branchId).orElse(null);
            attendances = attendanceRepository.findFiltered(branchId, null, null, null).list();
        } else {
            attendances = attendanceRepository.listAll();
        }

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE);
            Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(226, 232, 240));
            Font fontSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(15, 23, 42));
            Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(71, 85, 105));
            Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 9, new Color(15, 23, 42));

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(new Color(15, 23, 42));
            headerCell.setPadding(16);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Paragraph title = new Paragraph("NOVA TRANSFORM", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(title);

            Paragraph subtitle = new Paragraph("REPORTE OFICIAL DE ASISTENCIAS", fontSubtitle);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(subtitle);

            headerTable.addCell(headerCell);
            document.add(headerTable);
            document.add(new Paragraph(" "));

            if (member != null && member.getUsers() != null) {
                User u = member.getUsers();
                document.add(new Paragraph("Socio: " + u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")", fontSection));
            } else if (branch != null) {
                document.add(new Paragraph("Sucursal: " + branch.getName() + " - " + branch.getAddress(), fontSection));
            }
            document.add(new Paragraph("Total de registros de asistencia: " + attendances.size(), fontLabel));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{25f, 20f, 20f, 18f, 17f});

            addHeaderCell(table, "Socio", fontLabel);
            addHeaderCell(table, "Sucursal", fontLabel);
            addHeaderCell(table, "Fecha", fontLabel);
            addHeaderCell(table, "Entrada", fontLabel);
            addHeaderCell(table, "Salida", fontLabel);

            for (Attendance a : attendances) {
                String memberName = a.getIdMember() != null && a.getIdMember().getUsers() != null
                        ? a.getIdMember().getUsers().getFirstName() + " " + a.getIdMember().getUsers().getLastName()
                        : "N/A";
                String branchName = a.getIdBranch() != null ? a.getIdBranch().getName() : "N/A";

                addCell(table, memberName, fontValue);
                addCell(table, branchName, fontValue);
                addCell(table, a.getDate() != null ? a.getDate().toString() : "N/A", fontValue);
                addCell(table, a.getEntryTime() != null ? a.getEntryTime().toString() : "N/A", fontValue);
                addCell(table, a.getExitTime() != null ? a.getExitTime().toString() : "--:--", fontValue);
            }

            document.add(table);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de asistencias: " + e.getMessage(), e);
        }
    }

    private byte[] generateQrCodeBytes(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOut);
        return pngOut.toByteArray();
    }

    private Paragraph createSectionTitle(String text, Font font) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingBefore(4);
        p.setSpacingAfter(4);
        return p;
    }

    private void addTableCell(PdfPTable table, String label, String value, Font fontLabel, Font fontValue) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(3);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, fontValue));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPadding(3);
        table.addCell(cellValue);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(241, 245, 249));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }
}
