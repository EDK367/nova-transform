package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.payment.PaymentReceipt;

@ApplicationScoped
public class PaymentReceiptRepository implements PanacheRepositoryBase<PaymentReceipt, Long> {
}