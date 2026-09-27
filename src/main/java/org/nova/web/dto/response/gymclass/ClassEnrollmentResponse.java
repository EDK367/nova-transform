package org.nova.web.dto.response.gymclass;

import org.nova.domain.enums.EnrollmentStatus;

import java.time.Instant;
import java.util.UUID;

public record ClassEnrollmentResponse(
        Long id,
        Long classId,
        String classTypeName,
        Instant classSchedule,
        UUID memberId,
        String memberName,
        String memberEmail,
        EnrollmentStatus status,
        Instant enrollmentDate,
        Instant createdAt,
        Instant updatedAt
) {
}
