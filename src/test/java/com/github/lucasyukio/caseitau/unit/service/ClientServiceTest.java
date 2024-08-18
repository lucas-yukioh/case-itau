package com.github.lucasyukio.caseitau.unit.service;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientListResponse;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import com.github.lucasyukio.caseitau.repository.ClientRepository;
import com.github.lucasyukio.caseitau.service.impl.ClientServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void givenClientRequest_thenReturnClientResponse() {
        ClientRequest clientRequest = new ClientRequest(
                "Test", "123456", BigDecimal.ONE
        );

        UUID id = UUID.randomUUID();

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(getClient(id));

        ClientResponse clientResponse = new ClientResponse(
                id.toString(), "Test", "123456", BigDecimal.ONE
        );

        Assertions.assertThat(clientResponse).usingRecursiveComparison().isEqualTo(clientService.saveClient(clientRequest));
    }

    @Test
    public void givenGetClients_thenReturnClientListResponse() {
        UUID id = UUID.randomUUID();

        Mockito.when(clientRepository.findAll()).thenReturn(getClientList(id));

        ClientListResponse clientListResponse = new ClientListResponse(getClientResponseList(id));

        Assertions.assertThat(clientListResponse).usingRecursiveComparison().isEqualTo(clientService.getClients());
    }

    @Test
    public void givenAccountNumber_thenReturnClientResponse() {
        UUID id = UUID.randomUUID();

        Mockito.when(clientRepository.getClientByAccountNumber("123456")).thenReturn(Optional.of(getClient(id)));

        ClientResponse clientResponse = new ClientResponse(
                id.toString(), "Test", "123456", BigDecimal.ONE
        );

        Assertions.assertThat(clientResponse).usingRecursiveComparison().isEqualTo(clientService.getClientByAccountNumber("123456"));
    }

    private static Client getClient(UUID id) {
        Client client = new Client();
        client.setId(id);
        client.setName("Test");
        client.setAccountNumber("123456");
        client.setAccountBalance(BigDecimal.ONE);

        return client;
    }

    private static List<Client> getClientList(UUID id) {
        Client client1 = new Client();
        client1.setId(id);
        client1.setName("Client 1");
        client1.setAccountNumber("123456");
        client1.setAccountBalance(BigDecimal.ONE);

        Client client2 = new Client();
        client2.setId(id);
        client2.setName("Client 2");
        client2.setAccountNumber("654321");
        client2.setAccountBalance(BigDecimal.ONE);

        return List.of(client1, client2);
    }

    private static List<ClientResponse> getClientResponseList(UUID id) {
        ClientResponse clientResponse1 = new ClientResponse(
                id.toString(), "Client 1", "123456", BigDecimal.ONE
        );

        ClientResponse clientResponse2 = new ClientResponse(
                id.toString(), "Client 2", "654321", BigDecimal.ONE
        );

        return List.of(clientResponse1, clientResponse2);
    }
}
