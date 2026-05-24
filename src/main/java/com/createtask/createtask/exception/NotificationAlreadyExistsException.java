// NotificationAlreadyExistsException.java
package com.createtask.createtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Returns 409 Conflict
@ResponseStatus(HttpStatus.CONFLICT)
public class NotificationAlreadyExistsException extends RuntimeException {
    public NotificationAlreadyExistsException(String message) {
        super(message);
    }
}