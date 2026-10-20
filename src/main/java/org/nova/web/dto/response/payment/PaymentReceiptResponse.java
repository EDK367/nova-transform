package org.nova.web.dto.response.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentReceiptResponse(
        Long id,
        Long invoiceId,
        Long paymentMethodId,
        String paymentMethodName,
        Instant paymentDate,
        BigDecimal amountPaid,
        String reference,
        Instant createdAt,
        Instant updatedAt
) {
}
