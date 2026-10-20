package org.nova.web.dto.response.member;

import java.time.LocalDate;
import java.util.UUID;

public record MemberResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String username,
        String roleName,
        Long branchId,
        LocalDate registrationDate,
        boolean active
) {
}
