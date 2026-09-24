package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record CreateMembershipPlanRequest(
        @NotBlank(message = "El nombre del plan es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        @NotNull(message = "La duración en días es obligatoria")
        @Min(value = 1, message = "La duración mínima es de 1 día")
        Long duration,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal price,

        String description,

        List<Long> benefitIds
) {
}
