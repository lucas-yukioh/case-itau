package com.github.lucasyukio.caseitau.repository;

import com.github.lucasyukio.caseitau.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> getClientByAccountNumber(String accountNumber);
}
