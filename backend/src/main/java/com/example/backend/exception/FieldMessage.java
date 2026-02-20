package com.example.backend.exception;

import lombok.Getter;

public class FieldMessage {

    @Getter
    private String fieldName;

    @Getter
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
