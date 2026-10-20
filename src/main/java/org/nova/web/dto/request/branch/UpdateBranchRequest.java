package org.nova.web.dto.request.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBranchRequest(
        @NotBlank(message = "El nombre de la sucursal es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        @NotBlank(message = "La direccion es obligatoria")
        @Size(max = 255, message = "La direccion no puede exceder 255 caracteres")
        String address,

        @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
        String phone,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean active
) {}
