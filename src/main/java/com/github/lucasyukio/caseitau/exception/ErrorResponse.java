package com.github.lucasyukio.caseitau.exception;

import org.springframework.http.HttpStatusCode;

import java.util.List;

public record ErrorResponse(
        HttpStatusCode status,
        List<String> errors
) {
}
