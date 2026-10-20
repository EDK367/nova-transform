package org.nova.web.dto.response.trainer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TrainerAssignmentResponse(
        Long id,
        UUID memberId,
        String memberName,
        UUID trainerId,
        String trainerName,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        String reason,
        Instant createdAt,
        Instant updatedAt
) {}
