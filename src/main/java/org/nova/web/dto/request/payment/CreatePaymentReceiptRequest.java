package org.nova.web.dto.request.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record CreatePaymentReceiptRequest(
        @NotNull(message = "El ID de la factura es obligatorio")
        Long invoiceId,

        @NotNull(message = "El ID del método de pago es obligatorio")
        Long paymentMethodId,

        @NotNull(message = "El monto pagado es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto pagado debe ser mayor a cero")
        BigDecimal amountPaid,

        @Size(max = 150, message = "La referencia no puede exceder 150 caracteres")
        String reference,

        Instant paymentDate
) {
}
