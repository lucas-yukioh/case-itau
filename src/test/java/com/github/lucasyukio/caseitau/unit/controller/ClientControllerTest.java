package com.github.lucasyukio.caseitau.unit.controller;

import com.github.lucasyukio.caseitau.controller.ClientController;
import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    public void givenClientRequest_thenReturnClientResponse() {
        ClientRequest clientRequest = new ClientRequest(
                "Test", "123456", BigDecimal.ONE
        );

        ClientResponse clientResponse = new ClientResponse(
                "uuid", "Test", "123456", BigDecimal.ONE
        );

        Mockito.when(clientService.saveClient(Mockito.any(ClientRequest.class))).thenReturn(clientResponse);

        Assertions.assertEquals(HttpStatus.CREATED, clientController.saveClient(clientRequest).getStatusCode());
    }
}
