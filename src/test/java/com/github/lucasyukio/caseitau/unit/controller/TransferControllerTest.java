package com.github.lucasyukio.caseitau.unit.controller;

import com.github.lucasyukio.caseitau.controller.TransferController;
import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;
import com.github.lucasyukio.caseitau.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.github.lucasyukio.caseitau.util.TransferStatusEnum.COMPLETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @Test
    public void givenTransferRequest_thenReturnTransferResponse() {
        TransferRequest transferRequest = new TransferRequest(
                BigDecimal.ONE, "1", "2"
        );

        TransferResponse transferResponse = new TransferResponse(
                "uuid", COMPLETE, BigDecimal.ONE, "1", "2", "11/11/1111"
        );

        when(transferService.saveTransfer(transferRequest)).thenReturn(transferResponse);

        assertEquals(HttpStatus.CREATED, transferController.saveTransfer(transferRequest).getStatusCode());
    }
}
