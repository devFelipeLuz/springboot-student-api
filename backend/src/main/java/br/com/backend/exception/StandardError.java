package br.com.backend.exception;

import lombok.Getter;

import java.time.Instant;

public class StandardError {

    @Getter
    private Instant timestamp;

    @Getter
    private Integer status;

    @Getter
    private String error;

    @Getter
    private String message;

    @Getter
    private String path;

    public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
