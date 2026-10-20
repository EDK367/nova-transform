package org.nova.web.dto.request.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateRoutineExerciseRequest(
        @NotNull(message = "El ID de la rutina es obligatorio")
        Long routineId,

        @NotNull(message = "El ID del ejercicio es obligatorio")
        Long exerciseId,

        @NotBlank(message = "El dia de entrenamiento es obligatorio")
        @Size(max = 20, message = "El dia de entrenamiento no puede exceder 20 caracteres")
        String trainingDay,

        @NotNull(message = "Las series son obligatorias")
        Short sets,

        @NotNull(message = "Las repeticiones son obligatorias")
        Short reps,

        BigDecimal weight,

        Short rest
) {}
