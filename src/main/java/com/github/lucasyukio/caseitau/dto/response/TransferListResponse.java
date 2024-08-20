package com.github.lucasyukio.caseitau.dto.response;

import java.util.List;

public record TransferListResponse(
        List<TransferResponse> sent,
        List<TransferResponse> received
) {
}
