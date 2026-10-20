package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateMembershipRequest(
        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        @NotNull(message = "El ID del plan es obligatorio")
        Long planId,

        LocalDate startDate
) {
}
