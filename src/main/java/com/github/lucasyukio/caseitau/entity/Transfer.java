package com.github.lucasyukio.caseitau.entity;

import com.github.lucasyukio.caseitau.util.TransferStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatusEnum transferStatus;

    @Column(nullable = false)
    private BigDecimal transferAmount;

    @ManyToOne
    @JoinColumn(name = "sender_account", referencedColumnName = "accountNumber", nullable = false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account", referencedColumnName = "accountNumber", nullable = false)
    private Client receiver;

    @Column(nullable = false)
    private LocalDateTime createdDate;
}
