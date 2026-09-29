package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.nova.domain.enums.PromotionDiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePromotionRequest(
        @NotBlank(message = "El nombre de la promoción es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String name,

        String description,

        @NotNull(message = "El tipo de descuento es obligatorio")
        PromotionDiscountType discountType,

        @NotNull(message = "El valor del descuento es obligatorio")
        @DecimalMin(value = "0.01", message = "El descuento debe ser mayor a 0")
        BigDecimal discountValue,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate endDate,

        Boolean active
) {
}
