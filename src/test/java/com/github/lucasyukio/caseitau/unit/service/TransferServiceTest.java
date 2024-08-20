package com.github.lucasyukio.caseitau.unit.service;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.entity.Transfer;
import com.github.lucasyukio.caseitau.repository.ClientRepository;
import com.github.lucasyukio.caseitau.repository.TransferRepository;
import com.github.lucasyukio.caseitau.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.github.lucasyukio.caseitau.util.TransferStatusEnum.COMPLETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    public void givenTransferRequest_thenReturnTransferResponse() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "1", "2"
        );

        Client client = new Client();
        client.setAccountNumber("1");
        client.setAccountBalance(BigDecimal.TEN);

        UUID id = UUID.randomUUID();

        when(clientRepository.getClientByAccountNumber(transferRequest.senderAccount())).thenReturn(Optional.of(client));
        when(clientRepository.getClientByAccountNumber(transferRequest.receiverAccount())).thenReturn(Optional.of(client));
        when(transferRepository.save(any(Transfer.class))).thenReturn(getTransfer(id, client));

        TransferResponse transferResponse = new TransferResponse(
                id.toString(), COMPLETE, BigDecimal.ONE, client.getAccountNumber(), client.getAccountNumber(), ""
        );

        assertThat(transferResponse).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(transferService.saveTransfer(transferRequest));
    }

    @Test
    public void givenInvalidSenderAccount_thenReturnResponseStatusException() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "1", "2"
        );

        when(clientRepository.getClientByAccountNumber(transferRequest.senderAccount())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> transferService.saveTransfer(transferRequest));
    }

    @Test
    public void givenInvalidReceiverAccount_thenReturnResponseStatusException() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "1", "2"
        );

        Client client = new Client();
        client.setAccountNumber("1");
        client.setAccountBalance(BigDecimal.TEN);

        when(clientRepository.getClientByAccountNumber(transferRequest.senderAccount())).thenReturn(Optional.of(client));
        when(clientRepository.getClientByAccountNumber(transferRequest.receiverAccount())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> transferService.saveTransfer(transferRequest));
    }

    @Test
    public void givenExceededMaxTransferAmount_thenReturnResponseStatusException() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(20000), "1", "2"
        );

        Client client = new Client();
        client.setAccountNumber("1");
        client.setAccountBalance(BigDecimal.TEN);

        when(clientRepository.getClientByAccountNumber(transferRequest.senderAccount())).thenReturn(Optional.of(client));
        when(clientRepository.getClientByAccountNumber(transferRequest.receiverAccount())).thenReturn(Optional.of(client));

        Assertions.assertThrows(ResponseStatusException.class, () -> transferService.saveTransfer(transferRequest));
    }

    @Test
    public void givenInsufficientBalanceAmount_thenReturnResponseStatusException() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(5000), "1", "2"
        );

        Client client = new Client();
        client.setAccountNumber("1");
        client.setAccountBalance(BigDecimal.TEN);

        when(clientRepository.getClientByAccountNumber(transferRequest.senderAccount())).thenReturn(Optional.of(client));
        when(clientRepository.getClientByAccountNumber(transferRequest.receiverAccount())).thenReturn(Optional.of(client));

        Assertions.assertThrows(ResponseStatusException.class, () -> transferService.saveTransfer(transferRequest));
    }

    private static Transfer getTransfer(UUID id, Client client) {
        Transfer transfer = new Transfer();
        transfer.setId(id);
        transfer.setTransferStatus(COMPLETE);
        transfer.setTransferAmount(BigDecimal.ONE);
        transfer.setSender(client);
        transfer.setReceiver(client);
        transfer.setCreatedDate(LocalDateTime.now());

        return transfer;
    }
}
