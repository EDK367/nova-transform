package org.nova.web.dto.response.workout;

import java.time.Instant;

public record ExerciseResponse(
        Long id,
        String name,
        String description,
        String muscleGroup,
        Instant createdAt,
        Instant updatedAt
) {}
