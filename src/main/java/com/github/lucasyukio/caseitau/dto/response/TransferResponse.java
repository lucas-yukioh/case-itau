package com.github.lucasyukio.caseitau.dto.response;

import com.github.lucasyukio.caseitau.util.TransferStatusEnum;

import java.math.BigDecimal;

public record TransferResponse(
        String id,
        TransferStatusEnum transferStatus,
        BigDecimal transferAmount,
        String senderAccount,
        String receiverAccount,
        String createdDate
) {
}
