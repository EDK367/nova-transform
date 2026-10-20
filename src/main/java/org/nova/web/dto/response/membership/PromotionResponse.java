package org.nova.web.dto.response.membership;

import org.nova.domain.enums.PromotionDiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record PromotionResponse(
        Long id,
        String name,
        String description,
        PromotionDiscountType discountType,
        BigDecimal discountValue,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active,
        Instant createdAt
) {
}
