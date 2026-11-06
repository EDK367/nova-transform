package org.nova.web.dto.response.role;

import org.nova.web.dto.response.permission.PermissionResponse;

import java.util.List;

public record RoleResponse(
        Long id,
        String name,
        String description,
        boolean systemRole,
        List<PermissionResponse> permissions
) {
}
