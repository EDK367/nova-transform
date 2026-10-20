package org.nova.web.dto.response.gymclass;

import java.time.Instant;

public record ClassTypeResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
