package com.github.lucasyukio.caseitau.controller;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferListResponse;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> saveTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        return new ResponseEntity<>(transferService.saveTransfer(transferRequest), HttpStatus.CREATED);
    }

    @GetMapping("{accountNumber}")
    public ResponseEntity<TransferListResponse> getTransfersByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(transferService.getTransfersByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
