package org.nova.web.dto.request.member;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignMemberGoalRequest(
        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        @NotNull(message = "El ID del objetivo es obligatorio")
        Long goalId
) {}
