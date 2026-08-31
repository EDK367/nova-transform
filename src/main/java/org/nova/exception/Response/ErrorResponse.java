package org.nova.exception.Response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        List<String> messages
) {

    public static ErrorResponse of(
            int status,
            String error,
            String message
    ) {
        return new ErrorResponse(
                Instant.now().toString(),
                status,
                error,
                message,
                null
        );
    }

    public static ErrorResponse validation(
            int status,
            String error,
            List<String> messages
    ) {
        return new ErrorResponse(
                Instant.now().toString(),
                status,
                error,
                null,
                messages
        );
    }
}