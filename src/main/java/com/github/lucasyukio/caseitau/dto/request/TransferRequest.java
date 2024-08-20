package com.github.lucasyukio.caseitau.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull BigDecimal transferAmount,
        @NotBlank String senderAccount,
        @NotBlank String receiverAccount
        ) {
}
