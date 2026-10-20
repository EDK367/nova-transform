package org.nova.web.dto.request.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateExerciseRequest(
        @NotBlank(message = "El nombre del ejercicio es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description,

        @NotBlank(message = "El grupo muscular es obligatorio")
        @Size(max = 100, message = "El grupo muscular no puede exceder 100 caracteres")
        String muscleGroup
) {}
