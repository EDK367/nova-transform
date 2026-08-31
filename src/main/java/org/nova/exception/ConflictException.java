package org.nova.exception;

import jakarta.ws.rs.core.Response;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(Response.Status.CONFLICT, message);
    }

}