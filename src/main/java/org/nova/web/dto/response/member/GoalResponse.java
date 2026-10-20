package org.nova.web.dto.response.member;

import java.time.Instant;

public record GoalResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
