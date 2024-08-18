package com.github.lucasyukio.caseitau.service;

import com.github.lucasyukio.caseitau.dto.request.ClientRequest;
import com.github.lucasyukio.caseitau.dto.response.ClientResponse;

public interface ClientService {

    ClientResponse saveClient(ClientRequest clientRequest);
}
