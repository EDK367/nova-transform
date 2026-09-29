package org.nova;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.staff.Receptionist;
import org.nova.repository.BranchRepository;
import org.nova.repository.InvoiceRepository;
import org.nova.repository.MembershipRepository;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.repository.ReceptionistRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@ApplicationScoped
public class InvoiceAndPaymentTestHelper {

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    PaymentReceiptRepository receiptRepository;

    @Inject
    BranchRepository branchRepository;

    @Inject
    MembershipRepository membershipRepository;

    @Inject
    ReceptionistRepository receptionistRepository;

    @Transactional
    public Long createPendingInvoice(Long membershipId, Long branchId, UUID receptionistId, BigDecimal amount) {
        Membership membership = membershipRepository.findById(membershipId);
        Branch branch = branchRepository.findById(branchId);
        Receptionist receptionist = receptionistRepository.findById(receptionistId);

        Invoice invoice = new Invoice();
        invoice.setIdMembership(membership);
        invoice.setIdBranch(branch);
        invoice.setIdReceptionist(receptionist);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setAmount(amount);
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(15));
        invoice.setObservation("Factura de prueba para integración");

        invoiceRepository.persist(invoice);
        return invoice.getId();
    }

    @Transactional
    public void deleteInvoiceAndReceipts(Long invoiceId) {
        receiptRepository.delete("idInvoice.id", invoiceId);
        invoiceRepository.deleteById(invoiceId);
    }
}
