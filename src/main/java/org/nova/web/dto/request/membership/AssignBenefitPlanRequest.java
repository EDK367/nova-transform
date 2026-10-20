package org.nova.web.dto.request.membership;

import jakarta.validation.constraints.NotNull;

public record AssignBenefitPlanRequest(
        @NotNull(message = "El ID del plan es obligatorio")
        Long planId,

        @NotNull(message = "El ID del beneficio es obligatorio")
        Long benefitId
) {}
