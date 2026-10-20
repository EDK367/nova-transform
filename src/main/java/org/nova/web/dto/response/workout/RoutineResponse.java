package org.nova.web.dto.response.workout;

import java.time.Instant;
import java.util.UUID;

public record RoutineResponse(
        Long id,
        Long assignmentId,
        UUID memberId,
        String memberName,
        UUID trainerId,
        String trainerName,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
