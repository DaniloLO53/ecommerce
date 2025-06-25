package org.ecommerce.project.exceptions;

public class APIForbiddenException extends RuntimeException {
    public APIForbiddenException(String message) {
        super(message);
    }
}
