package org.nova.web.dto.request.gymclass;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAttendanceRequest(
        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,

        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        UUID receptionistId,

        LocalDate date,

        LocalTime entryTime,

        LocalTime exitTime
) {
}
