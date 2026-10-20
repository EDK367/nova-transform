package org.nova.web.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGoalRequest(
        @NotBlank(message = "El nombre del objetivo es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description
) {}
