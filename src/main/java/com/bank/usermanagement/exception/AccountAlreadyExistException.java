package com.bank.usermanagement.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String s) {
        super(s);
    }
}
