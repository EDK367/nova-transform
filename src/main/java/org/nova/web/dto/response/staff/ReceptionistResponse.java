package org.nova.web.dto.response.staff;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReceptionistResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String username,
        Long branchId,
        String branchName,
        LocalDate hireDate,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
