package com.github.lucasyukio.caseitau.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record ClientRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp= "[0-9]+", message = "must be only numbers") String accountNumber,
        @NotNull BigDecimal accountBalance
) {
}
