package org.nova.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.nova.exception.Response.ErrorResponse;

import java.util.List;

@Provider
public class ValidationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse response = ErrorResponse.validation(
                400,
                "Validation Failed",
                errors
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }

}