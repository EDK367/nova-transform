package org.nova.web.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateRoleRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 100, message = "El nombre del rol no puede superar 100 caracteres")
        String name,

        String description,

        List<Long> permissionIds
) {
}
