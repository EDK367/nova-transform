package org.nova.web.dto.response.membership;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record PlanPriceHistoryResponse(
        Long id,
        Long planId,
        BigDecimal price,
        LocalDate startDate,
        LocalDate endDate,
        String observation,
        Instant createdAt
) {
}
