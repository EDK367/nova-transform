package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateClassRequest(
        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,

        @NotNull(message = "El ID del tipo de clase es obligatorio")
        Long classTypeId,

        @NotNull(message = "El ID del entrenador es obligatorio")
        UUID trainerId,

        @NotNull(message = "El horario de la clase es obligatorio")
        Instant schedule,

        @NotNull(message = "La duración de la clase es obligatoria")
        @Min(value = 1, message = "La duración debe ser de al menos 1 minuto")
        Long duration,

        @NotNull(message = "La capacidad máxima es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser de al menos 1 persona")
        Long maxCapacity
) {
}
