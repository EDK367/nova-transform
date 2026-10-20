package org.nova.web.dto.request.user;

import jakarta.validation.constraints.NotNull;
import org.nova.domain.enums.TokenType;

import java.util.UUID;

public record CreateUserTokenRequest(
        @NotNull(message = "El ID del usuario es obligatorio")
        UUID userId,

        @NotNull(message = "El tipo de token es obligatorio")
        TokenType type,

        Long durationHours
) {}
