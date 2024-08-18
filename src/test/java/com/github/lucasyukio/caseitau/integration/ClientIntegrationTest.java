package com.github.lucasyukio.caseitau.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenClientRequest_whenSaveClient_thenReturnStatus201() throws Exception {
        ClientRequest clientRequest = new ClientRequest(
                "Test", "123456", BigDecimal.ONE
        );

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.accountNumber", is("123456")))
                .andExpect(jsonPath("$.accountBalance", is(BigDecimal.ONE.intValue())));
    }

    @Test
    public void givenClientRequestWithNullValues_whenSaveClient_thenReturnStatus400() throws Exception {
        ClientRequest clientRequest = new ClientRequest(
                null, null, null
        );

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("name: must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("accountNumber: must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("accountBalance: must not be null")));
    }

    @Test
    public void givenClientRequestWithSameAccountNumber_whenSaveClient_thenReturnStatus400() throws Exception {
        ClientRequest clientRequest = new ClientRequest(
                "Test", "123456", BigDecimal.ONE
        );

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber", is("123456")))
                .andDo(
                        result -> mockMvc.perform(post("/clients")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(clientRequest)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                                .andExpect(jsonPath("$.errors", hasSize(1)))
                                .andExpect(jsonPath("$.errors", hasItem("Account number already exists")))
                );
    }
}
