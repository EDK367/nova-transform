package org.nova.web.dto.response.payment;

import java.time.Instant;

public record PaymentMethodResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
