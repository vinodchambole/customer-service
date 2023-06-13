package com.bank.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;
    private String transactionPassword;

    // Constructors, getters, and setters

}

