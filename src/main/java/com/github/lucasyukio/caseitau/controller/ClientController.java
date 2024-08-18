package com.github.lucasyukio.caseitau.controller;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> saveClient(@RequestBody @Valid ClientRequest clientRequest) {
        return new ResponseEntity<>(clientService.saveClient(clientRequest), HttpStatus.CREATED);
    }
}
