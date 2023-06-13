package com.bank.usermanagement.service;

import com.bank.usermanagement.dto.AccountCreationRequest;
import com.bank.usermanagement.dto.AccountCreationResponse;
import com.bank.usermanagement.entity.Account;
import com.bank.usermanagement.entity.Customer;
import com.bank.usermanagement.exception.AccountAlreadyExistException;
import com.bank.usermanagement.repository.AccountRepository;
import com.bank.usermanagement.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAllAccountsByEmail(String email) {
        return accountRepository.findAllByEmail(email);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public AccountCreationResponse createAccount(AccountCreationRequest accountCreationRequest) {
        // Check if the account already exists
        if (accountRepository.findByAccountNumber(accountCreationRequest.getAccountNumber()).isPresent()) {
            throw new AccountAlreadyExistException("User account already exists: " + accountCreationRequest.getAccountNumber());
        }

        // Retrieve the user by email
        User userByEmail = userService.getUserByEmail(accountCreationRequest.getEmail());

        // Create a new customer and populate its data
        Customer customer = new Customer();
        customer.setEmail(accountCreationRequest.getEmail());
        customer.setName(userByEmail.getFirstname() + " " + userByEmail.getLastname());
        customer.setEmail(accountCreationRequest.getEmail());

        String transactionPassword = generateRandomPassword();

        customer.setTransactionPassword(transactionPassword);
        customerService.saveCustomer(customer);

        // Create a new account and populate its data
        Account account = populateAccount(accountCreationRequest, customer);
        // Save the account
        Account save = accountRepository.save(account);

        return  AccountCreationResponse.builder().accountId(save.getId()).accountNumber(accountCreationRequest.getAccountNumber())
                .accountType(accountCreationRequest.getAccountType())
                .email(accountCreationRequest.getEmail())
                .transactionPassword(transactionPassword).build();
    }

    public String generateRandomPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    private Account populateAccount(AccountCreationRequest accountCreationRequest, Customer c) {
        Account a = new Account();
        a.setBalance(0);
        a.setAccountNumber(accountCreationRequest.getAccountNumber());
        a.setCustomer(c);
        a.setAccountType(accountCreationRequest.getAccountType());
        a.setAccountNumber(accountCreationRequest.getAccountNumber());
        a.setCreatedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());
        a.setEmail(accountCreationRequest.getEmail());
        a.setBalance(accountCreationRequest.getAmount());
        return a;
    }

    public Optional<Account> updateAccount(Long id, Account account) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();
            existingAccount.setAccountNumber(account.getAccountNumber());
            existingAccount.setBalance(account.getBalance());
            // Update other account properties as needed
            return Optional.of(accountRepository.save(existingAccount));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

