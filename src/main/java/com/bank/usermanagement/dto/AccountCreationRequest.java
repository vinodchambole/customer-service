package com.bank.usermanagement.dto;

import com.bank.usermanagement.entity.AccountType;
import lombok.Data;


@Data
public class AccountCreationRequest {
    private String email;
    private String accountNumber;
    private AccountType accountType;
    private double amount;
}
