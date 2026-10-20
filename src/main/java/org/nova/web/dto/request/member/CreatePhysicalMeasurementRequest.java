package org.nova.web.dto.request.member;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePhysicalMeasurementRequest(
        @NotNull(message = "El ID del socio es obligatorio")
        UUID memberId,

        LocalDate date,
        BigDecimal weight,
        BigDecimal height,
        BigDecimal bodyFatPercentage,
        BigDecimal muscleMass,
        BigDecimal waist,
        BigDecimal arm,
        BigDecimal leg,
        String observations
) {}
