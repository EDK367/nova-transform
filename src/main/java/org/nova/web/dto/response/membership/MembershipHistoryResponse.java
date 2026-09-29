package org.nova.web.dto.response.membership;

import org.nova.domain.enums.MembershipStatus;

import java.time.Instant;

public record MembershipHistoryResponse(
        Long id,
        Long membershipId,
        MembershipStatus previousStatus,
        MembershipStatus newStatus,
        Instant changeDate,
        String observation
) {
}
