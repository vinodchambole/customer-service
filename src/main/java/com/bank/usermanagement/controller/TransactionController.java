package com.bank.usermanagement.controller;

import com.bank.usermanagement.dto.TransactionDto;
import com.bank.usermanagement.dto.TransactionRequest;
import com.bank.usermanagement.dto.TransferRequest;
import com.bank.usermanagement.entity.Transaction;
import com.bank.usermanagement.security.user.User;
import com.bank.usermanagement.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Transaction> addBalance(@AuthenticationPrincipal User user, @RequestBody TransactionRequest transactionRequest) {
        log.info("User name: " + user.getUsername());
        Transaction transaction = transactionService.addBalance(user, transactionRequest);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Transaction> withdrawBalance(@AuthenticationPrincipal User user, @RequestBody TransactionRequest transactionRequest) {
        log.info("User name: " + user.getUsername());
        Transaction transaction = transactionService.withdrawBalance(user, transactionRequest);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer-balance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    public ResponseEntity<TransactionDto> transferBalance(@AuthenticationPrincipal User user, @RequestBody TransferRequest transferRequest) {
        log.info("User name: " + user.getUsername());
        TransactionDto transaction = transactionService.transferBalance(user, transferRequest);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionsByUserName(@AuthenticationPrincipal User user, @RequestParam String accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserName(user, accountId));
    }
}
