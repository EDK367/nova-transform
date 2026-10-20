package org.nova.web.dto.response.trainer;

import java.time.Instant;

public record SpecializationResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
