package com.ecollado.samples.jerseyrest.exceptions;

public class FileResourceServiceException extends Exception {
    public FileResourceServiceException() {
    }

    public FileResourceServiceException(String message) {
        super(message);
    }

    public FileResourceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileResourceServiceException(Throwable cause) {
        super(cause);
    }

    public FileResourceServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
