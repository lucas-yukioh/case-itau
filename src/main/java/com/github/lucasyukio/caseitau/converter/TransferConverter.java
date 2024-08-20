package com.github.lucasyukio.caseitau.converter;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.entity.Transfer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TransferConverter {

    public static Transfer toEntity(TransferRequest transferRequest) {
        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferRequest.transferAmount());
        transfer.setCreatedDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        return transfer;
    }

    public static TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId().toString(),
                transfer.getTransferStatus(),
                transfer.getTransferAmount(),
                transfer.getSender().getAccountNumber(),
                transfer.getReceiver().getAccountNumber(),
                transfer.getCreatedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"))
        );
    }
}
