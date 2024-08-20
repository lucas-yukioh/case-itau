package com.github.lucasyukio.caseitau.service;

import com.github.lucasyukio.caseitau.dto.request.TransferRequest;
import com.github.lucasyukio.caseitau.dto.response.TransferResponse;

public interface TransferService {

     TransferResponse saveTransfer(TransferRequest transferRequest);
}
