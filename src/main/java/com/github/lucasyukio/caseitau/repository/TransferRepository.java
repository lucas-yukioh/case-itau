package com.github.lucasyukio.caseitau.repository;

import com.github.lucasyukio.caseitau.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
