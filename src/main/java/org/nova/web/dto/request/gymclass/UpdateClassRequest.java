package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.Min;
import org.nova.domain.enums.ClassStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateClassRequest(
        Long branchId,
        Long classTypeId,
        UUID trainerId,
        Instant schedule,
        @Min(value = 1, message = "La duración debe ser de al menos 1 minuto")
        Long duration,
        @Min(value = 1, message = "La capacidad debe ser de al menos 1 persona")
        Long maxCapacity,
        ClassStatus status
) {
}
