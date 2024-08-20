package com.github.lucasyukio.caseitau.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageEnum {

    CLIENT_NOT_FOUND("Client not found"),
    SENDER_CLIENT_NOT_FOUND("Sender client not found"),
    RECEIVER_CLIENT_NOT_FOUND("Receiver client not found"),
    TRANSFER_MAX_AMOUNT_EXCEEDED("Transfer amount must not exceed R$10.000,00"),
    INSUFFICIENT_BALANCE_AMOUNT("Sender balance amount is insufficient for this transfer");

    private final String message;
}
