package org.nova.web.dto.response.membership;

import org.nova.domain.enums.MembershipStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MembershipResponse(
        Long id,
        UUID memberId,
        String memberName,
        String memberEmail,
        Long planId,
        String planName,
        Long planDuration,
        BigDecimal planPrice,
        MembershipStatus status,
        LocalDate startDate,
        LocalDate dueDate,
        Instant createdAt
) {
}
