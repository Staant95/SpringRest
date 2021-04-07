package com.smartshop.exceptionHandlers;

public class InvalidLoginCredentialsException extends RuntimeException {
    public InvalidLoginCredentialsException() {
        super("Invalid email or password");
    }
}
