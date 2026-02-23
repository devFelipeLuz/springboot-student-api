package br.com.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Business rule violation",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;

        ValidationError err = new ValidationError(
                Instant.now(),
                status.value(),
                "Validation error",
                "Invalid fields",
                request.getRequestURI()
        );

        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericExcpetion(
        Exception ex,
        HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Unexpected error",
                "An unexpected erro occurred",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
