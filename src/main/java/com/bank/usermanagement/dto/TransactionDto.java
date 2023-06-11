package com.bank.usermanagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String Status;
}
