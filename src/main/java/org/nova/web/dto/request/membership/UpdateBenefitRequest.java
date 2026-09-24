package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBenefitRequest(
        @NotBlank(message = "El nombre del beneficio es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String name,

        String description
) {
}
