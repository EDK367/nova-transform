package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateClassRatingRequest(
        @NotNull(message = "El ID de la inscripción es obligatorio")
        Long enrollmentId,

        @NotNull(message = "La calificación es obligatoria")
        @Min(value = 1, message = "La calificación mínima es 1")
        @Max(value = 5, message = "La calificación máxima es 5")
        Short rating,

        String comment
) {
}
