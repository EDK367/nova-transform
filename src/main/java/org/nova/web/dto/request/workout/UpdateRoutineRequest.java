package org.nova.web.dto.request.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRoutineRequest(
        @NotBlank(message = "El nombre de la rutina es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description,

        Boolean active
) {}
