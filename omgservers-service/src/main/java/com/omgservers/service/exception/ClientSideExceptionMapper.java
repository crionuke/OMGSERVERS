package com.omgservers.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import java.io.IOException;

@Slf4j
public class ClientSideExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    final ObjectMapper objectMapper;

    public ClientSideExceptionMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RuntimeException toThrowable(Response response) {
        final var statusCode = response.getStatus();
        final var responseAsString = response.readEntity(String.class);

        try {
            final var exceptionErrorResponse = objectMapper.readValue(responseAsString, ExceptionErrorResponse.class);

            if (statusCode >= 400 && statusCode < 500) {
                if (statusCode == Response.Status.BAD_REQUEST.getStatusCode()) {
                    return new ClientSideBadRequestException(exceptionErrorResponse);
                } else if (statusCode == Response.Status.UNAUTHORIZED.getStatusCode()) {
                    return new ClientSideUnauthorizedException(exceptionErrorResponse);
                } else if (statusCode == Response.Status.NOT_FOUND.getStatusCode()) {
                    return new ClientSideNotFoundException(exceptionErrorResponse);
                } else if (statusCode == Response.Status.CONFLICT.getStatusCode()) {
                    return new ClientSideConflictException(exceptionErrorResponse);
                } else if (statusCode == Response.Status.GONE.getStatusCode()) {
                    return new ClientSideGoneException(exceptionErrorResponse);
                }
            } else if (statusCode >= 500) {
                return new ClientSideInternalException(exceptionErrorResponse);
            }

        } catch (IOException e) {
            log.warn("Client side exception mapping failed, {}", e.getMessage());
        }

        return new ClientSideHttpException(statusCode, responseAsString);
    }
}
