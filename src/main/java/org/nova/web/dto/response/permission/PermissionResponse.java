package org.nova.web.dto.response.permission;

public record PermissionResponse(
        Long id,
        String code,
        String name,
        String module,
        String description
) {
}
