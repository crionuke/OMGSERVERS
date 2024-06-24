package com.omgservers.service.exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Objects;

@Slf4j
@ApplicationScoped
public class ServerSideExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> illegalArgumentException(final IllegalArgumentException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(ExceptionQualifierEnum.ARGUMENT_WRONG);
        return RestResponse.status(Response.Status.BAD_REQUEST, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> constraintViolationException(final ConstraintViolationException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(ExceptionQualifierEnum
                .VALIDATION_CONSTRAINT_VIOLATED);
        return RestResponse.status(Response.Status.BAD_REQUEST, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> webApplicationException(final WebApplicationException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        if (Objects.nonNull(e.getResponse()) && e.getResponse().getStatus() == 400) {
            final var exceptionErrorResponse = new ExceptionErrorResponse(ExceptionQualifierEnum.REQUEST_WRONG);
            return RestResponse.status(Response.Status.BAD_REQUEST, exceptionErrorResponse);
        } else {
            return throwable(e);
        }
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> notSupportedException(final NotSupportedException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(ExceptionQualifierEnum.MEDIA_TYPE_WRONG);
        return RestResponse.status(Response.Status.BAD_REQUEST, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> badRequestException(final ServerSideBadRequestException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.BAD_REQUEST, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> unauthorizedServerException(final ServerSideUnauthorizedException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.UNAUTHORIZED, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> forbiddenServerException(final ServerSideForbiddenException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.FORBIDDEN, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> notFoundException(final ServerSideNotFoundException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.NOT_FOUND, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> conflictException(final ServerSideConflictException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.CONFLICT, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> goneException(final ServerSideGoneException e) {
        log.debug("Server side exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.GONE, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> internalException(final ServerSideInternalException e) {
        log.error("Internal exception, {}:{}", e.getClass().getSimpleName(), e.getMessage());

        final var exceptionErrorResponse = new ExceptionErrorResponse(e);
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, exceptionErrorResponse);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionErrorResponse> throwable(final Throwable e) {
        log.error("Uncaught exception, {}:{}", e.getClass().getSimpleName(), e.getMessage(), e);

        final var exceptionErrorResponse = new ExceptionErrorResponse(ExceptionQualifierEnum
                .INTERNAL_EXCEPTION_OCCURRED);
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, exceptionErrorResponse);
    }
}
