package org.nova.web.dto.request.trainer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTrainerRequest(
        @NotNull(message = "La fecha de contratacion es obligatoria")
        LocalDate hireDate,

        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String firstName,

        @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
        String lastName,

        @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
        String phone,

        Long branchId,

        Boolean active
) {}
