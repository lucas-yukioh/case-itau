package com.github.lucasyukio.caseitau.service.impl;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientListResponse;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.repository.ClientRepository;
import com.github.lucasyukio.caseitau.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.lucasyukio.caseitau.converter.ClientConverter.toEntity;
import static com.github.lucasyukio.caseitau.converter.ClientConverter.toResponse;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientResponse saveClient(ClientRequest clientRequest) {
        Client newClient = toEntity(clientRequest);

        return toResponse(clientRepository.save(newClient));
    }

    @Override
    public ClientListResponse getClients() {
        List<ClientResponse> clientList = new ArrayList<>();

        clientRepository.findAll().forEach(client -> clientList.add(toResponse(client)));

        return new ClientListResponse(clientList);
    }

    @Override
    public ClientResponse getClientByAccountNumber(String accountNumber) {
        Optional<Client> optionalClient = clientRepository.getClientByAccountNumber(accountNumber);

        return toResponse(optionalClient.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")));
    }
}
