package org.nova.web.dto.request.workout;

import java.math.BigDecimal;

public record UpdateRoutineExerciseRequest(
        String trainingDay,
        Short sets,
        Short reps,
        BigDecimal weight,
        Short rest
) {}
