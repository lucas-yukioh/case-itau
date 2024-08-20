package com.github.lucasyukio.caseitau.controller;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientListResponse;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.dto.response.TransferListResponse;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.exception.ErrorResponse;
import com.github.lucasyukio.caseitau.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Transfer Controller")
@RestController
@RequestMapping("transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(summary = "Save a new transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer successfully saved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TransferResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request: <br> " +
                    "- Values are invalid <br>" +
                    "- Sender or receiver account number is invalid <br>" +
                    "- Transfer amount is above R$10.000,00 <br>" +
                    "- Sender account doesn't have enough funds", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<TransferResponse> saveTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        return new ResponseEntity<>(transferService.saveTransfer(transferRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of sent and received transfer by the account number in descending order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TransferListResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request: <br>" +
                    "- Account number not found in database", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("{accountNumber}")
    public ResponseEntity<TransferListResponse> getTransfersByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(transferService.getTransfersByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
