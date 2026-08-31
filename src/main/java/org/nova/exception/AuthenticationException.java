package org.nova.exception;

import jakarta.ws.rs.core.Response;

public class AuthenticationException extends ApiException {

    public AuthenticationException(String message) {
        super(Response.Status.UNAUTHORIZED, message);
    }

}