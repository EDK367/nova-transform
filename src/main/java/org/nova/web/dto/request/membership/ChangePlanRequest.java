package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.NotNull;

public record ChangePlanRequest(
        @NotNull(message = "El ID del nuevo plan es obligatorio")
        Long newPlanId,

        String reason
) {
}
