package com.github.lucasyukio.caseitau.unit.converter;

import com.github.lucasyukio.caseitau.converter.ClientConverter;
import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;
import com.github.lucasyukio.caseitau.entity.Client;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConverterTest {

    @Test
    public void givenClientRequest_thenReturnClientEntity() {
        ClientRequest clientRequest = new ClientRequest(
                "Test", "123456", BigDecimal.ONE
        );

        Client client = new Client();
        client.setName("Test");
        client.setAccountNumber("123456");
        client.setAccountBalance(BigDecimal.ONE);

        assertThat(client).usingRecursiveComparison().ignoringFields("createdDate").isEqualTo(ClientConverter.toEntity(clientRequest));
    }

    @Test
    public void givenClientEntity_thenReturnClientResponse() {
        UUID id = UUID.randomUUID();

        Client client = new Client();
        client.setId(id);
        client.setName("Test");
        client.setAccountNumber("123456");
        client.setAccountBalance(BigDecimal.ONE);

        ClientResponse clientResponse = new ClientResponse(
                id.toString(), "Test", "123456", BigDecimal.ONE
        );

        assertThat(clientResponse).usingRecursiveComparison().isEqualTo(ClientConverter.toResponse(client));
    }
}
