package org.nova.web.dto.response.gymclass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AttendanceResponse(
        Long id,
        Long branchId,
        String branchName,
        UUID receptionistId,
        String receptionistName,
        UUID memberId,
        String memberName,
        String memberEmail,
        LocalDate date,
        LocalTime entryTime,
        LocalTime exitTime,
        Instant createdAt,
        Instant updatedAt
) {
}
