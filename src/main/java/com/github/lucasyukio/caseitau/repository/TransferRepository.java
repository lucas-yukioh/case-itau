package com.github.lucasyukio.caseitau.repository;

import com.github.lucasyukio.caseitau.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT t FROM Transfer t WHERE t.sender.accountNumber = :accountNumber OR t.receiver.accountNumber = :accountNumber ORDER BY t.createdDate DESC")
    List<Transfer> findAllTransfersByAccountNumber(String accountNumber);
}
