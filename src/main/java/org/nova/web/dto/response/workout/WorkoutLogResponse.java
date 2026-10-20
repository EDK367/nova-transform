package org.nova.web.dto.response.workout;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record WorkoutLogResponse(
        Long id,
        UUID memberId,
        String memberName,
        Long routineExerciseId,
        Long exerciseId,
        String exerciseName,
        String routineName,
        Instant date,
        Short actualSets,
        Short actualReps,
        BigDecimal actualWeight,
        String observation,
        Instant createdAt,
        Instant updatedAt
) {}
