package org.nova.service.payment;

import org.nova.common.PageResponse;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.web.dto.request.payment.CreatePaymentReceiptRequest;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;

import java.util.List;
import java.util.Optional;

public interface PaymentReceiptService {

    List<PaymentReceipt> findAll();

    Optional<PaymentReceipt> findById(Long id);

    PaymentReceipt create(PaymentReceipt paymentReceipt);

    PaymentReceipt update(Long id, PaymentReceipt paymentReceipt);

    void delete(Long id);

    PaymentReceiptResponse createReceipt(CreatePaymentReceiptRequest request);

    PaymentReceiptResponse findResponseById(Long id);

    List<PaymentReceiptResponse> findByInvoiceId(Long invoiceId);

    PageResponse<PaymentReceiptResponse> findPaged(int page, int size, Long invoiceId, String sortBy, String sortDirection);
}