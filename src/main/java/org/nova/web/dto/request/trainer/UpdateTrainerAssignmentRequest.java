package org.nova.web.dto.request.trainer;

import java.time.LocalDate;

public record UpdateTrainerAssignmentRequest(
        LocalDate startDate,
        LocalDate endDate,
        Boolean active,
        String reason
) {}
