package com.github.lucasyukio.caseitau.service.impl;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.repository.ClientRepository;
import com.github.lucasyukio.caseitau.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
