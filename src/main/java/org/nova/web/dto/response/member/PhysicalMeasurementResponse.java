package org.nova.web.dto.response.member;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PhysicalMeasurementResponse(
        Long id,
        UUID memberId,
        String memberName,
        LocalDate date,
        BigDecimal weight,
        BigDecimal height,
        BigDecimal bodyFatPercentage,
        BigDecimal muscleMass,
        BigDecimal waist,
        BigDecimal arm,
        BigDecimal leg,
        String observations,
        Instant createdAt,
        Instant updatedAt
) {}
