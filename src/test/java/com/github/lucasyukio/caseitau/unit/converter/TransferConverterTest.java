package com.github.lucasyukio.caseitau.unit.converter;

import com.github.lucasyukio.caseitau.converter.TransferConverter;
import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.entity.Transfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.github.lucasyukio.caseitau.util.TransferStatusEnum.COMPLETE;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferConverterTest {

    @Test
    public void givenTransferRequest_thenReturnTransferEntity() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "1", "2"
        );

        Transfer transfer = new Transfer();
        transfer.setTransferAmount(BigDecimal.ONE);

        assertThat(transfer).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(TransferConverter.toEntity(transferRequest));
    }

    @Test
    public void givenTransferEntity_thenReturnTransferResponse() {
        UUID id = UUID.randomUUID();

        Client client = new Client();
        client.setAccountNumber("1");

        LocalDateTime localDateTime = LocalDateTime.of(1, 1, 1, 1, 1);

        Transfer transfer = new Transfer();
        transfer.setId(id);
        transfer.setTransferStatus(COMPLETE);
        transfer.setTransferAmount(BigDecimal.ONE);
        transfer.setSender(client);
        transfer.setReceiver(client);
        transfer.setCreatedDate(localDateTime);

        TransferResponse transferResponse = new TransferResponse(
                id.toString(), COMPLETE, BigDecimal.ONE, client.getAccountNumber(), client.getAccountNumber(), localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"))
        );

        assertThat(transferResponse).usingRecursiveComparison().isEqualTo(TransferConverter.toResponse(transfer));
    }
}
