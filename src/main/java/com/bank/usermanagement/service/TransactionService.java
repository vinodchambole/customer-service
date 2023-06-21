package com.bank.usermanagement.service;

import com.bank.usermanagement.dto.TransactionDto;
import com.bank.usermanagement.dto.TransactionRequest;
import com.bank.usermanagement.dto.TransferRequest;
import com.bank.usermanagement.entity.*;
import com.bank.usermanagement.exception.AccountNotFoundException;
import com.bank.usermanagement.exception.InsufficientBalanceException;
import com.bank.usermanagement.exception.UnauthorizedUserException;
import com.bank.usermanagement.repository.AccountRepository;
import com.bank.usermanagement.repository.CustomerRepository;
import com.bank.usermanagement.repository.TransactionRepository;
import com.bank.usermanagement.security.user.Role;
import com.bank.usermanagement.security.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public Transaction addBalance(User user, TransactionRequest transactionRequest) {

        Account account = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (user.getRole().equals(Role.ADMIN) || (Objects.equals(account.getCustomer().getEmail(), user.getUsername())) && validateTransactionPassword(transactionRequest.getAccountId(), user, transactionRequest.getTransactionPassword())) {

            BigDecimal amount = BigDecimal.valueOf(transactionRequest.getAmount());
            BigDecimal newBalance = BigDecimal.valueOf(account.getBalance()).add(amount);
            account.setBalance(newBalance.doubleValue());

            Transaction transaction = Transaction.builder()
                    .account(account)
                    .amount(transactionRequest.getAmount())
                    .createdAt(LocalDateTime.now())
                    .transactionType(TransactionType.ADD)
                    .actor(user.getFirstname() + "-" + user.getRole())
                    .build();
            return transactionRepository.save(transaction);
        } else {
            throw new UnauthorizedUserException("unauthorized user.");
        }

    }

    public Transaction withdrawBalance(User user, TransactionRequest transactionRequest) {
        Account account = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (user.getRole().equals(Role.ADMIN) || (Objects.equals(account.getCustomer().getEmail(), user.getUsername())) && validateTransactionPassword(transactionRequest.getAccountId(), user, transactionRequest.getTransactionPassword())) {

            double amount = transactionRequest.getAmount();
            BigDecimal newBalance = BigDecimal.valueOf(account.getBalance()).subtract(BigDecimal.valueOf(amount));
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Insufficient balance");
            }
            account.setBalance(newBalance.doubleValue());
            Transaction transaction = Transaction.builder()
                    .createdAt(LocalDateTime.now())
                    .transactionType(TransactionType.WITHDRAW)
                    .account(account)
                    .amount(amount)
                    .actor(user.getFirstname() + "-" + user.getRole())
                    .build();
            return transactionRepository.save(transaction);
        } else {
            throw new UnauthorizedUserException("unauthorized user.");
        }
    }

    private Boolean validateTransactionPassword(Long accountId, User user, String transactionPassword) {
        return customerRepository.findByEmail(user.getUsername())
                .stream().filter(customer -> customer.getAccounts().stream().anyMatch(account -> accountId.compareTo(account.getId()) == 0))
                .findFirst()
                .map(Customer::getTransactionPassword)
                .map(password -> password.equals(transactionPassword))
                .orElseThrow(() -> new UnauthorizedUserException("Invalid transaction password"));
    }

    public TransactionDto transferBalance(User user, TransferRequest transferRequest) {
        Account fromAccount = accountRepository.findById(transferRequest.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));

        Account toAccount = accountRepository.findById(transferRequest.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        TransactionStatus status;

        if (user.getRole().equals(Role.ADMIN) || (Objects.equals(fromAccount.getCustomer().getEmail(), user.getUsername())) && validateTransactionPassword(transferRequest.getFromAccountId(), user, transferRequest.getTransactionPassword())) {

            try {

                BigDecimal amount = BigDecimal.valueOf(transferRequest.getAmount());
                BigDecimal fromAccountBalance = BigDecimal.valueOf(fromAccount.getBalance());
                if (fromAccountBalance.compareTo(amount) < 0) {

                    throw new InsufficientBalanceException("Insufficient balance in the from account");
                }

                BigDecimal newFromAccountBalance = fromAccountBalance.subtract(amount);
                fromAccount.setBalance(newFromAccountBalance.doubleValue());

                BigDecimal toAccountBalance = BigDecimal.valueOf(toAccount.getBalance());
                BigDecimal newToAccountBalance = toAccountBalance.add(amount);
                toAccount.setBalance(newToAccountBalance.doubleValue());


                status = TransactionStatus.SUCCESS;
            } catch (Exception e) {
                status = TransactionStatus.FAILED;
            }
            LocalDateTime transactionTime = LocalDateTime.now();

            Transaction transactionFromAccount = Transaction.builder()
                    .createdAt(transactionTime)
                    .transactionType(TransactionType.TRANSFER)
                    .account(fromAccount)
                    .amount(transferRequest.getAmount())
                    .toAccount(toAccount.getAccountNumber())
                    .transactionStatus(status)
                    .actor(user.getFirstname() + "-" + user.getRole())
                    .fromAccount(fromAccount.getAccountNumber()).build();

            Transaction transactionToAccount = Transaction.builder()
                    .createdAt(transactionTime)
                    .transactionType(TransactionType.CREDIT)
                    .account(toAccount)
                    .amount(transferRequest.getAmount())
                    .toAccount(toAccount.getAccountNumber())
                    .transactionStatus(status)
                    .actor(user.getFirstname() + "-" + user.getRole())
                    .fromAccount(fromAccount.getAccountNumber()).build();

            transactionRepository.saveAll(List.of(transactionFromAccount, transactionToAccount));

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setAmount(transferRequest.getAmount());
            transactionDto.setStatus(status.name());
            transactionDto.setFromAccountId(transferRequest.getFromAccountId());
            transactionDto.setToAccountId(transferRequest.getToAccountId());

            return transactionDto;
        } else {
            throw new UnauthorizedUserException("unauthorized user.");
        }
    }

    public List<Transaction> getTransactionsByUserName(User user, String accountId) {
        Optional<Account> byId = accountRepository.findById(Long.parseLong(accountId));
        if (byId.isPresent()) {
            Account account = byId.get();
            if (user.getRole().equals(Role.ADMIN) || account.getCustomer().getEmail().equals(user.getUsername())) {
                return account.getTransactions();
            } else {
                throw new UnauthorizedUserException("unauthorized user.");
            }
        } else {
            throw new AccountNotFoundException("account does not exist");
        }
    }
}

