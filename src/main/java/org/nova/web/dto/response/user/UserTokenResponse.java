package org.nova.web.dto.response.user;

import org.nova.domain.enums.TokenType;

import java.time.Instant;
import java.util.UUID;

public record UserTokenResponse(
        Long id,
        UUID userId,
        String username,
        String userEmail,
        String token,
        TokenType type,
        Instant generationDate,
        Instant expirationDate,
        boolean used,
        Instant createdAt,
        Instant updatedAt
) {}
