package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClassTypeRequest(
        @NotBlank(message = "El nombre del tipo de clase es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description
) {
}
