package org.nova.web.dto.response.payment;

import org.nova.domain.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceResponse(
        Long id,
        Long branchId,
        String branchName,
        Long membershipId,
        UUID memberId,
        String memberName,
        String memberEmail,
        Long planId,
        String planName,
        UUID receptionistId,
        String receptionistName,
        InvoiceStatus status,
        Long promotionId,
        String promotionName,
        BigDecimal originalAmount,
        BigDecimal discountAmount,
        BigDecimal amount,
        BigDecimal amountPaid,
        BigDecimal balanceDue,
        LocalDate issueDate,
        LocalDate dueDate,
        String observation,
        Instant createdAt,
        Instant updatedAt
) {
}
