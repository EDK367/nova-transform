package org.nova.service.payment;

import org.nova.domain.model.payment.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {

    List<PaymentMethod> findAll();

    Optional<PaymentMethod> findById(Long id);

    PaymentMethod create(PaymentMethod paymentMethod);

    PaymentMethod update(Long id, PaymentMethod paymentMethod);

    void delete(Long id);
}