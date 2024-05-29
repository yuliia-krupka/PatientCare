package com.pcare.pcare.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    public ValidationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
