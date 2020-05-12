package com.citi.Exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String uuid) {
        super("User " + uuid + " not found");
    }
}
