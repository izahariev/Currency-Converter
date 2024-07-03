package com.example.currencyconverter.errors;

/**
 * An exception used for invalid page
 *
 * @author Ivaylo Zahariev
 */
public class InvalidPageNumberException extends Exception {
    public InvalidPageNumberException() {super("Page must be 0 or greater");}
}
