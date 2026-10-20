package org.nova.web.dto.response.trainer;

import java.time.Instant;
import java.util.UUID;

public record TrainerSpecializationResponse(
        Long id,
        UUID trainerId,
        String trainerName,
        Long specializationId,
        String specializationName,
        Instant createdAt,
        Instant updatedAt
) {}
