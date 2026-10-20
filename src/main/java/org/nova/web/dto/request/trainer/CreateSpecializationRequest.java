package org.nova.web.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSpecializationRequest(
        @NotBlank(message = "El nombre de la especializacion es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description
) {}
