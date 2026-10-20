package org.nova.web.dto.response.membership;

import java.time.Instant;
import java.time.LocalDate;

public record MembershipFreezeResponse(
        Long id,
        Long membershipId,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        Instant createdAt
) {
}
