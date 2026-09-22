package org.nova.service.payment;

import org.nova.domain.model.payment.PaymentReceipt;

import java.util.List;
import java.util.Optional;

public interface PaymentReceiptService {

    List<PaymentReceipt> findAll();

    Optional<PaymentReceipt> findById(Long id);

    PaymentReceipt create(PaymentReceipt paymentReceipt);

    PaymentReceipt update(Long id, PaymentReceipt paymentReceipt);

    void delete(Long id);
}