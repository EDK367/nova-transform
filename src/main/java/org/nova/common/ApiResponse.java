package org.nova.common;

import java.time.Instant;

public record ApiResponse<T>(
        int status,
        String message,
        T data,
        Instant timestamp
) {

    public ApiResponse(int status, String message, T data) {
        this(status, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Operación exitosa", data);
    }
}
