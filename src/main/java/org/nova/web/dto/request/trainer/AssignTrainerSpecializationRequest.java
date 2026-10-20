package org.nova.web.dto.request.trainer;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignTrainerSpecializationRequest(
        @NotNull(message = "El ID del entrenador es obligatorio")
        UUID trainerId,

        @NotNull(message = "El ID de la especializacion es obligatorio")
        Long specializationId
) {}
