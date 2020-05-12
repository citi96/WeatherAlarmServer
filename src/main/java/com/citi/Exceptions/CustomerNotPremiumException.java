package com.citi.Exceptions;

public class CustomerNotPremiumException extends Exception {
    public CustomerNotPremiumException(String message){
        super(message);
    }
}
