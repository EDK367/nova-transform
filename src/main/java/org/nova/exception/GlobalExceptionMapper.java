package org.nova.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.nova.exception.Response.ErrorResponse;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof ApiException apiException) {

            Response.Status status = apiException.getStatus();

            ErrorResponse error = ErrorResponse.of(
                    status.getStatusCode(),
                    status.getReasonPhrase(),
                    apiException.getMessage()
            );

            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        exception.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(
                        ErrorResponse.of(
                                500,
                                "Internal Server Error",
                                exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred"
                        )
                )
                .build();
    }

}