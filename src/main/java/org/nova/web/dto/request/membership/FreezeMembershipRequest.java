package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record FreezeMembershipRequest(
        @NotNull(message = "La fecha de inicio del congelamiento es obligatoria")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin del congelamiento es obligatoria")
        LocalDate endDate,

        String reason
) {
}
