package com.github.lucasyukio.caseitau.dto.response;

import java.util.List;

public record ClientListResponse(
        List<ClientResponse> clients
) {
}
