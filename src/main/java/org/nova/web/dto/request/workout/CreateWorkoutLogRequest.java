package org.nova.web.dto.request.workout;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateWorkoutLogRequest(
        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        @NotNull(message = "El ID del ejercicio de rutina es obligatorio")
        Long routineExerciseId,

        Instant date,

        Short actualSets,

        Short actualReps,

        BigDecimal actualWeight,

        String observation
) {}
