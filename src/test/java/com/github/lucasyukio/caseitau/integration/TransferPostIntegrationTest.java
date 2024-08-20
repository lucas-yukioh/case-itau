package com.github.lucasyukio.caseitau.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.INSUFFICIENT_BALANCE_AMOUNT;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.RECEIVER_CLIENT_NOT_FOUND;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.SENDER_CLIENT_NOT_FOUND;
import static com.github.lucasyukio.caseitau.util.ErrorMessageEnum.TRANSFER_MAX_AMOUNT_EXCEEDED;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferPostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenTransferRequest_whenSaveTransfer_thenReturnStatus201() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "123456", "654321"
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.transferStatus", is("COMPLETE")))
                .andExpect(jsonPath("$.transferAmount", is(BigDecimal.ONE.intValue())))
                .andExpect(jsonPath("$.senderAccount", is("123456")))
                .andExpect(jsonPath("$.receiverAccount", is("654321")))
                .andDo(
                        result -> mockMvc.perform(get("/clients/123456")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accountBalance", is(BigDecimal.valueOf(999.00).doubleValue())))
                )
                .andDo(
                        result -> mockMvc.perform(get("/clients/654321")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accountBalance", is(BigDecimal.valueOf(5001.00).doubleValue())))
                );
    }

    @Test
    public void givenSimultaneousTransferRequests_whenSaveTransfer_thenReturnStatus201AndStatus400() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(800), "123456", "654321"
        );

        CountDownLatch latch = new CountDownLatch(1);

        CompletableFuture<Void> firstTransfer = CompletableFuture.runAsync(() ->{
            try {
                mockMvc.perform(post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transferRequest)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is(notNullValue())))
                        .andExpect(jsonPath("$.transferStatus", is("COMPLETE")))
                        .andExpect(jsonPath("$.transferAmount", is(BigDecimal.valueOf(800).intValue())))
                        .andExpect(jsonPath("$.senderAccount", is("123456")))
                        .andExpect(jsonPath("$.receiverAccount", is("654321")));

                latch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> secondTransfer = CompletableFuture.runAsync(() ->{
            try {
                latch.await();
                
                mockMvc.perform(post("/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transferRequest)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                        .andExpect(jsonPath("$.errors", hasSize(1)))
                        .andExpect(jsonPath("$.errors", hasItem(INSUFFICIENT_BALANCE_AMOUNT.getMessage())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.allOf(firstTransfer, secondTransfer).join();
    }

    @Test
    public void givenTransferRequestWithNullValues_whenSaveTransfer_thenReturnStatus400() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                null, null, null
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("transferAmount: must not be null")))
                .andExpect(jsonPath("$.errors", hasItem("senderAccount: must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("receiverAccount: must not be blank")));
    }

    @Test
    public void givenInvalidSenderAccountNumber_whenSaveTransfer_thenReturnStatus400() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(500), "000000", "654321"
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(SENDER_CLIENT_NOT_FOUND.getMessage())));
    }

    @Test
    public void givenInvalidReceiverAccountNumber_whenSaveTransfer_thenReturnStatus400() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(500), "123456", "000000"
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(RECEIVER_CLIENT_NOT_FOUND.getMessage())));
    }

    @Test
    public void givenExceededMaxTransferValue_whenSaveTransfer_thenReturnStatus400AndSaveIncompleteTransfer() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(20000), "999888", "123456"
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(TRANSFER_MAX_AMOUNT_EXCEEDED.getMessage())))
                .andDo(
                        result -> mockMvc.perform(get("/transfers/999888")
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.sent", hasSize(1)))
                                .andExpect(jsonPath("$.sent[0].transferStatus", is("INCOMPLETE")))
                );
    }

    @Test
    public void givenInsufficientBalanceAmount_whenSaveTransfer_thenReturnStatus400() throws Exception {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.valueOf(2000), "123456", "654321"
        );

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(INSUFFICIENT_BALANCE_AMOUNT.getMessage())));
    }
}
