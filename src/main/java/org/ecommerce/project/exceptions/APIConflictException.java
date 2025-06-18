package org.ecommerce.project.exceptions;

public class APIConflictException extends RuntimeException {
    public APIConflictException(String message) {
        super(message);
    }
}
