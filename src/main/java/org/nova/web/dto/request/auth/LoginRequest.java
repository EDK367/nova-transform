package org.nova.web.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Usuario o correo requerido")
        @Size(min = 3, max = 150, message = "El usuario o correo debe tener entre 3 y 150 caracteres")
        String username,

        @NotBlank(message = "Contraseña requerida")
        @Size(min = 6, max = 100, message = "La contraseña solo permite un tamaño de 6 a 100 caracteres")
        String password
) { }
