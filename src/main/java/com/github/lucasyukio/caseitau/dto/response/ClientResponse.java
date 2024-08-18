package com.github.lucasyukio.caseitau.dto.response;

import java.math.BigDecimal;

public record ClientResponse(
        String id,
        String name,
        String accountNumber,
        BigDecimal accountBalance
) {
}
