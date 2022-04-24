package com.levimartines.codewithspring.handlers;

import com.levimartines.codewithspring.exceptions.AuthorizationException;
import com.levimartines.codewithspring.exceptions.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException e, HttpServletRequest request) {
        StandardError err = new StandardError(System.currentTimeMillis(),
            HttpStatus.NOT_FOUND.value(), "Not found",
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorizationException(AuthorizationException e, HttpServletRequest request) {
        StandardError err = new StandardError(System.currentTimeMillis(),
            HttpStatus.FORBIDDEN.value(), "Forbidden",
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e,
                                                      HttpServletRequest request) {
        ValidationError err = new ValidationError(System.currentTimeMillis(),
            HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error",
            "Invalid input", request.getRequestURI());
        for (FieldError fieldErr : e.getBindingResult().getFieldErrors()) {
            err.putError(fieldErr.getField(), fieldErr.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> exception(Exception e, HttpServletRequest request) {
        StandardError err = new StandardError(System.currentTimeMillis(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
