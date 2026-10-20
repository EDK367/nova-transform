package org.nova.web.dto.response.member;

import java.time.Instant;
import java.util.UUID;

public record MemberGoalResponse(
        Long id,
        UUID memberId,
        String memberName,
        Long goalId,
        String goalName,
        String goalDescription,
        Instant createdAt,
        Instant updatedAt
) {}
