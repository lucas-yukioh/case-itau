package com.github.lucasyukio.caseitau.service;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferListResponse;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;

public interface TransferService {

     TransferResponse saveTransfer(TransferRequest transferRequest);
     TransferListResponse getTransfersByAccountNumber(String accountNumber);
}
