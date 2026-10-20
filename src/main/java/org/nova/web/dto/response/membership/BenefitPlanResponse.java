package org.nova.web.dto.response.membership;

import java.time.Instant;

public record BenefitPlanResponse(
        Long id,
        Long planId,
        String planName,
        Long benefitId,
        String benefitName,
        String benefitDescription,
        Instant createdAt,
        Instant updatedAt
) {}
