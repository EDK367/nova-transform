package org.nova.web.dto.response.workout;

import java.math.BigDecimal;
import java.time.Instant;

public record RoutineExerciseResponse(
        Long id,
        Long routineId,
        String routineName,
        Long exerciseId,
        String exerciseName,
        String muscleGroup,
        String trainingDay,
        Short sets,
        Short reps,
        BigDecimal weight,
        Short rest,
        Instant createdAt,
        Instant updatedAt
) {}
