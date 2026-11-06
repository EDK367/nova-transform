package org.nova.web.dto.request.role;

import java.util.List;

public record AssignRolePermissionsRequest(
        List<Long> permissionIds
) {
}
