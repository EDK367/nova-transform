package org.nova.web.dto.response.branch;

import java.time.LocalDateTime;

public record BranchResponse(
        Long id,
        String name,
        String address,
        String phone,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
