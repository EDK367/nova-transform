package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.payment.PaymentReceipt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentReceiptRepository implements PanacheRepositoryBase<PaymentReceipt, Long> {

    public Optional<PaymentReceipt> findByIdWithDetails(Long id) {
        return find("select r from PaymentReceipt r " +
                "join fetch r.idPaymentMethod pm " +
                "join fetch r.idInvoice i " +
                "join fetch i.idMembership m " +
                "join fetch m.idMember mem " +
                "join fetch mem.users memUser " +
                "where r.id = ?1", id).firstResultOptional();
    }

    public List<PaymentReceipt> findByInvoiceId(Long invoiceId) {
        return find("select r from PaymentReceipt r " +
                "join fetch r.idPaymentMethod pm " +
                "where r.idInvoice.id = ?1 order by r.paymentDate desc", invoiceId).list();
    }

    public BigDecimal sumPaidByInvoiceId(Long invoiceId) {
        BigDecimal sum = getEntityManager().createQuery(
                "select coalesce(sum(r.amountPaid), 0) from PaymentReceipt r where r.idInvoice.id = :invoiceId",
                BigDecimal.class)
                .setParameter("invoiceId", invoiceId)
                .getSingleResult();
        return sum != null ? sum : BigDecimal.ZERO;
    }

    public long countByPaymentMethodId(Long paymentMethodId) {
        return count("idPaymentMethod.id", paymentMethodId);
    }

    public PanacheQuery<PaymentReceipt> findFiltered(Long invoiceId, Sort sort) {
        if (invoiceId != null) {
            return find("select r from PaymentReceipt r join fetch r.idPaymentMethod pm where r.idInvoice.id = :invoiceId",
                    sort, Parameters.with("invoiceId", invoiceId));
        }
        return find("select r from PaymentReceipt r join fetch r.idPaymentMethod pm", sort);
    }
}