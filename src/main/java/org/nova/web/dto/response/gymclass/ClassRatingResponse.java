package org.nova.web.dto.response.gymclass;

import java.time.Instant;
import java.util.UUID;

public record ClassRatingResponse(
        Long id,
        Long enrollmentId,
        Long classId,
        String classTypeName,
        UUID memberId,
        String memberName,
        Short rating,
        String comment,
        Instant dateTime,
        Instant createdAt,
        Instant updatedAt
) {
}
