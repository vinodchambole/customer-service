package com.bank.usermanagement.dto;

import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;
    private String Status;
}
