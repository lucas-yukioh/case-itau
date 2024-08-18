package com.github.lucasyukio.caseitau.converter;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.entity.Client;

public class ClientConverter {

    public static Client toEntity(ClientRequest clientRequest) {
        Client client = new Client();
        client.setName(clientRequest.name());
        client.setAccountNumber(clientRequest.accountNumber());
        client.setAccountBalance(clientRequest.accountBalance());

        return client;
    }

    public static ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId().toString(),
                client.getName(),
                client.getAccountNumber(),
                client.getAccountBalance()
        );
    }
}
