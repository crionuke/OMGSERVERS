package com.omgservers.application.exception;

public class ServerSideForbiddenException extends ServerSideClientErrorException {
    public ServerSideForbiddenException() {
        super();
    }

    public ServerSideForbiddenException(String message) {
        super(message);
    }

    public ServerSideForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerSideForbiddenException(Throwable cause) {
        super(cause);
    }

    protected ServerSideForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
