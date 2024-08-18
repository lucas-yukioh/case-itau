package com.github.lucasyukio.caseitau.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_account_number", columnNames = { "accountNumber" }))
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String accountNumber;

    private BigDecimal accountBalance;

    @CreationTimestamp
    private LocalDateTime createdDate;
}

