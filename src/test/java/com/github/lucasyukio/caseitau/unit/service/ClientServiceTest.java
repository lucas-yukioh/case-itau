package com.github.lucasyukio.caseitau.unit.service;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
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

    private static Client getClient(UUID id) {
        Client client = new Client();
        client.setId(id);
        client.setName("Test");
        client.setAccountNumber("123456");
        client.setAccountBalance(BigDecimal.ONE);

        return client;
    }
}
