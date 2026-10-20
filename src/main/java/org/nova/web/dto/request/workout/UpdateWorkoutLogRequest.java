package org.nova.web.dto.request.workout;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdateWorkoutLogRequest(
        Instant date,
        Short actualSets,
        Short actualReps,
        BigDecimal actualWeight,
        String observation
) {}
