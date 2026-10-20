package org.nova.web.dto.request.trainer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainerRequest(
        UUID userId,

        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String firstName,

        @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
        String lastName,

        @Email(message = "El formato de correo no es valido")
        @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
        String email,

        @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
        String username,

        @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
        String phone,

        Long branchId,

        @NotNull(message = "La fecha de contratacion es obligatoria")
        LocalDate hireDate
) {}
