package org.nova.service.payment;

import org.nova.domain.model.payment.PaymentMethod;
import org.nova.web.dto.request.payment.CreatePaymentMethodRequest;
import org.nova.web.dto.request.payment.UpdatePaymentMethodRequest;
import org.nova.web.dto.response.payment.PaymentMethodResponse;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {

    List<PaymentMethod> findAll();

    Optional<PaymentMethod> findById(Long id);

    PaymentMethod create(PaymentMethod paymentMethod);

    PaymentMethod update(Long id, PaymentMethod paymentMethod);

    void delete(Long id);

    List<PaymentMethodResponse> findAllResponses();

    PaymentMethodResponse findResponseById(Long id);

    PaymentMethodResponse create(CreatePaymentMethodRequest request);

    PaymentMethodResponse update(Long id, UpdatePaymentMethodRequest request);
}