package com.github.lucasyukio.caseitau.service.impl;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferListResponse;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.entity.Transfer;
import com.github.lucasyukio.caseitau.repository.ClientRepository;
import com.github.lucasyukio.caseitau.repository.TransferRepository;
import com.github.lucasyukio.caseitau.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.github.lucasyukio.caseitau.converter.TransferConverter.toEntity;
import static com.github.lucasyukio.caseitau.converter.TransferConverter.toResponse;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.CLIENT_NOT_FOUND;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.INSUFFICIENT_BALANCE_AMOUNT;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.RECEIVER_CLIENT_NOT_FOUND;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.SENDER_CLIENT_NOT_FOUND;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.TRANSFER_MAX_AMOUNT_EXCEEDED;
import static com.github.lucasyukio.caseitau.util.TransferStatusEnum.COMPLETE;
import static com.github.lucasyukio.caseitau.util.TransferStatusEnum.INCOMPLETE;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
public class TransferServiceImpl implements TransferService {

    private static final BigDecimal TRANSFER_MAX_AMOUNT = BigDecimal.valueOf(10000);

    private final TransferRepository transferRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, ClientRepository clientRepository) {
        this.transferRepository = transferRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public TransferResponse saveTransfer(TransferRequest transferRequest) {
        Client senderClient = clientRepository.findByAccountNumber(transferRequest.senderAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, SENDER_CLIENT_NOT_FOUND.getMessage()));

        Client receiverClient = clientRepository.findByAccountNumber(transferRequest.receiverAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, RECEIVER_CLIENT_NOT_FOUND.getMessage()));

        String errorMessage = validateTransferAmount(transferRequest.transferAmount(), senderClient.getAccountBalance());

        Transfer newTransfer = toEntity(transferRequest);
        newTransfer.setSender(senderClient);
        newTransfer.setReceiver(receiverClient);

        if (isNotBlank(errorMessage)) {
            newTransfer.setTransferStatus(INCOMPLETE);
            transferRepository.save(newTransfer);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        newTransfer.setTransferStatus(COMPLETE);

        senderClient.setAccountBalance(senderClient.getAccountBalance().subtract(newTransfer.getTransferAmount()));
        receiverClient.setAccountBalance(receiverClient.getAccountBalance().add(newTransfer.getTransferAmount()));

        clientRepository.save(senderClient);
        clientRepository.save(receiverClient);

        return toResponse(transferRepository.save(newTransfer));
    }

    @Override
    @Transactional
    public TransferListResponse getTransfersByAccountNumber(String accountNumber) {
        Client client = clientRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENT_NOT_FOUND.getMessage()));

        List<Transfer> transferList = transferRepository.findAllTransfersByAccountNumber(client.getAccountNumber());
        List<TransferResponse> sentTransferList = new ArrayList<>();
        List<TransferResponse> receivedTransferList = new ArrayList<>();

        transferList.forEach(transfer -> {
            if (transfer.getSender().getAccountNumber().equals(accountNumber)) {
                sentTransferList.add(toResponse(transfer));
            } else if (transfer.getReceiver().getAccountNumber().equals(accountNumber) && COMPLETE.equals(transfer.getTransferStatus())) {
                receivedTransferList.add(toResponse(transfer));
            }
        });

        return new TransferListResponse(sentTransferList, receivedTransferList);
    }

    private static String validateTransferAmount(BigDecimal transferValue, BigDecimal senderAccountBalance) {
        if (TRANSFER_MAX_AMOUNT.compareTo(transferValue) < 0) {
            return TRANSFER_MAX_AMOUNT_EXCEEDED.getMessage();
        }

        if (senderAccountBalance.compareTo(transferValue) < 0) {
            return INSUFFICIENT_BALANCE_AMOUNT.getMessage();
        }

        return null;
    }
}
