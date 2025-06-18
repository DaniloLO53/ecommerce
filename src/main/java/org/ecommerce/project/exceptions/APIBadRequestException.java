package org.ecommerce.project.exceptions;

public class APIBadRequestException extends RuntimeException {
    public APIBadRequestException(String message) {
        super(message);
    }
}
