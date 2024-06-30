package com.example.currencyconverter.errors;

/**
 * An exception used for invalid amounts
 *
 * @author Ivaylo Zahariev
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super("Amount must be 0 or greater");
    }
}
