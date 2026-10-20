package org.nova.web.dto.request.trainer;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainerAssignmentRequest(
        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        @NotNull(message = "El ID del entrenador es obligatorio")
        UUID trainerId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startDate,

        LocalDate endDate,

        String reason
) {}
