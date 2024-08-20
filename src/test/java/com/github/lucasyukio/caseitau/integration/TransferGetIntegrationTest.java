package com.github.lucasyukio.caseitau.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.CLIENT_NOT_FOUND;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferGetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenAccountNumber_whenGetTransfers_thenReturnStatus200() throws Exception {
        mockMvc.perform(get("/transfers/123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sent", hasSize(1)))
                .andExpect(jsonPath("$.sent[0].senderAccount", is("123456")))
                .andExpect(jsonPath("$.received", hasSize(1)))
                .andExpect(jsonPath("$.received[0].receiverAccount", is("123456")));
    }

    @Test
    public void givenInvalidAccountNumber_whenGetTransfers_thenReturnStatus400() throws Exception {
        mockMvc.perform(get("/transfers/000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(CLIENT_NOT_FOUND.getMessage())));
    }
}
