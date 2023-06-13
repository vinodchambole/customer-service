package com.bank.usermanagement.repository;

import com.bank.usermanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    // You can define custom query methods or use the ones provided by JpaRepository
    // Additional methods can be added here

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAllByEmail(String email);

}
