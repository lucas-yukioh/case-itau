package com.github.lucasyukio.caseitau.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.CLIENT_NOT_FOUND;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientGetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenGet_whenGetClients_thenReturnStatus200() throws Exception {
        mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients", hasSize(3)))
                .andExpect(jsonPath("$.clients[*].name", containsInAnyOrder("John", "Peter", "Parker")));
    }

    @Test
    public void givenAccountNumber_whenGetClient_thenReturnStatus200() throws Exception {
        mockMvc.perform(get("/clients/123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    public void givenInvalidAccountNumber_whenGetClient_thenReturnStatus404() throws Exception {
        mockMvc.perform(get("/clients/000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(CLIENT_NOT_FOUND.getMessage())));
    }
}
