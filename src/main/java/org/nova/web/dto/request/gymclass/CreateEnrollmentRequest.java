package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEnrollmentRequest(
        @NotNull(message = "El ID de la clase es obligatorio")
        Long classId,

        UUID memberId
) {
}
