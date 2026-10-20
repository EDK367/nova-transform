package org.nova.web.dto.request.member;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdatePhysicalMeasurementRequest(
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
