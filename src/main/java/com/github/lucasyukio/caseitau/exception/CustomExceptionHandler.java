package com.github.lucasyukio.caseitau.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors())
            errors.add(error.getField() + ": " + error.getDefaultMessage());

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors())
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());

        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }

    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, List.of("Account number already exists"));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.status());
    }
}
