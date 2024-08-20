package com.github.lucasyukio.caseitau.controller;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientListResponse;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.exception.ErrorResponse;
import com.github.lucasyukio.caseitau.service.ClientService;
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

@Tag(name = "Client Controller")
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Save a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client successfully saved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request: <br>" +
                    "- Values are invalid <br>" +
                    "- Account number already exists in database", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<ClientResponse> saveClient(@RequestBody @Valid ClientRequest clientRequest) {
        return new ResponseEntity<>(clientService.saveClient(clientRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of clients")
    @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientListResponse.class)) })
    @GetMapping
    public ResponseEntity<ClientListResponse> getClients() {
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @Operation(summary = "Get a client by the account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found: <br>" +
                    "- Account number not found in database", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("{accountNumber}")
    public ResponseEntity<ClientResponse> getClientByAccountNumber(@PathVariable String accountNumber) {
        return new ResponseEntity<>(clientService.getClientByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
