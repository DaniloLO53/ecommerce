package org.ecommerce.project.exception;

public class APIException extends RuntimeException {
//    private static final Long SERIAL_VERSION_UID = 1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
