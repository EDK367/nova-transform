package org.nova.web.dto.response.gymclass;

import org.nova.domain.enums.ClassStatus;

import java.time.Instant;
import java.util.UUID;

public record ClassResponse(
        Long id,
        Long branchId,
        String branchName,
        Long classTypeId,
        String classTypeName,
        UUID trainerId,
        String trainerName,
        ClassStatus status,
        Instant schedule,
        Long duration,
        Long maxCapacity,
        Long enrolledCount,
        Long availableSpots,
        Long waitlistCount,
        Double averageRating,
        Instant createdAt,
        Instant updatedAt
) {
}
