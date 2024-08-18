package com.github.lucasyukio.caseitau.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ClientRequest(
        @NotBlank String name,
        @NotBlank String accountNumber,
        @NotNull BigDecimal accountBalance
) {
}
