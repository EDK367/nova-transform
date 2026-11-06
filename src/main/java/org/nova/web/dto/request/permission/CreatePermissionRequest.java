package org.nova.web.dto.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePermissionRequest(
        @NotBlank(message = "El código del permiso es obligatorio")
        @Size(max = 100, message = "El código no puede superar 100 caracteres")
        String code,

        @NotBlank(message = "El nombre del permiso es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String name,

        @NotBlank(message = "El módulo del permiso es obligatorio")
        @Size(max = 100, message = "El módulo no puede superar 100 caracteres")
        String module,

        String description
) {
}
