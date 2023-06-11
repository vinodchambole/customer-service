package com.bank.usermanagement.controller;

import com.bank.usermanagement.dto.AccountCreationRequest;
import com.bank.usermanagement.dto.AccountCreationResponse;
import com.bank.usermanagement.entity.Account;
import com.bank.usermanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> accountOptional = accountService.getAccountById(id);
        return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest account) {
        AccountCreationResponse createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody AccountCreationRequest account) {
//        Optional<Account> accountOptional = accountService.getAccountById(id);
//        if (accountOptional.isPresent()) {
//            Account existingAccount = accountOptional.get();
////            existingAccount.setAccountNumber(account.getAccountNumber());
////            existingAccount.setBalance(account.getBalance());
//            // Update other account properties as needed
//            Account updatedAccount = accountService.createAccount(account);
//            return ResponseEntity.ok(updatedAccount);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        Optional<Account> accountOptional = accountService.getAccountById(id);
        if (accountOptional.isPresent()) {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
