package org.nova.common;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String message,
        String error,
        Instant timestamp
) {

    public ErrorResponse(int status, String message, String error) {
        this(status, message, error, Instant.now());
    }
}
