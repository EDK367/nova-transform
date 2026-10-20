package org.nova.web.dto.response.member;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMemberResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String username,
        String role,
        Long branchId,
        LocalDate registrationDate,
        boolean active,
        String temporaryPassword
) {
}
