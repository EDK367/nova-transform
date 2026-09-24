package org.nova.web.dto.response.user;

import java.time.LocalDate;
import java.util.UUID;

public record CreateUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String username,
        String role,
        Long branchId,
        LocalDate hireDate,
        boolean active,
        String temporaryPassword
) {
}
