package org.nova.exception;

import jakarta.ws.rs.core.Response;

public class AuthorizationException extends ApiException {

    public AuthorizationException(String message) {
        super(Response.Status.FORBIDDEN, message);
    }

}