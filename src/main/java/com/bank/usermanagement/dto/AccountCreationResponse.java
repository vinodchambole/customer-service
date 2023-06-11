package com.bank.usermanagement.dto;

import com.bank.usermanagement.entity.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationResponse {
    private Long accountId;
    private String email;
    private String accountNumber;
    private AccountType accountType;
    private String transactionPassword;
}
