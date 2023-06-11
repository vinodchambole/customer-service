package com.bank.usermanagement.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String s) {
        super(s);
    }
}
