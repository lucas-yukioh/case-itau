package com.github.lucasyukio.caseitau.repository;

import com.github.lucasyukio.caseitau.entity.Client;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Client> findByAccountNumber(String accountNumber);
}
