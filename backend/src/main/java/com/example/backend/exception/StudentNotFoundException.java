package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentNotFoundException extends ResponseStatusException {

    public StudentNotFoundException(HttpStatus code, String message) {
        super(code, message);
    }
}
