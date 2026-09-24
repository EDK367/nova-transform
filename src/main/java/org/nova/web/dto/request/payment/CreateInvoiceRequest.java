package org.nova.web.dto.request.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateInvoiceRequest(
        @NotNull(message = "El ID de la membresía es obligatorio")
        Long membershipId,

        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,

        UUID receptionistId,

        Long promotionId,

        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal amount,

        LocalDate issueDate,

        LocalDate dueDate,

        String observation
) {
}
