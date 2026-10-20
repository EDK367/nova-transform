package org.nova.web.dto.response.gymclass;

import java.time.Instant;
import java.util.UUID;

public record WaitlistResponse(
        Long id,
        Long classId,
        String classTypeName,
        Instant classSchedule,
        UUID memberId,
        String memberName,
        String memberEmail,
        Instant registrationDate,
        Boolean notified,
        Instant createdAt,
        Instant updatedAt
) {
}
