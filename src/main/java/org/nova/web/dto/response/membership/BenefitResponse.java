package org.nova.web.dto.response.membership;

import java.time.Instant;

public record BenefitResponse(
        Long id,
        String name,
        String description,
        Instant createdAt
) {
}
