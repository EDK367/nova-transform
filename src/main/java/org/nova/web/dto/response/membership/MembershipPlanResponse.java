package org.nova.web.dto.response.membership;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record MembershipPlanResponse(
        Long id,
        String name,
        Long duration,
        BigDecimal price,
        String description,
        List<BenefitResponse> benefits,
        Instant createdAt
) {
}
