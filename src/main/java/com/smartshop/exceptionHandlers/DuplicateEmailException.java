package com.smartshop.exceptionHandlers;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("Sorry, we already have a user with that email");
    }
}
