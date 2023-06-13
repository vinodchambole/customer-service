package com.bank.usermanagement.dto;

import lombok.Data;



@Data
public class TransactionRequest {
    private Long accountId;
    private double amount;
    private String transactionPassword;
}
