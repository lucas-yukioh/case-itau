package com.github.lucasyukio.caseitau.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        HttpStatus status,
        List<String> errors
) {
}
