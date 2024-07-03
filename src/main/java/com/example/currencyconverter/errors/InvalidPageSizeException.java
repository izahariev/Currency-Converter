package com.example.currencyconverter.errors;

/**
 * An exception used for invalid page size
 *
 * @author Ivaylo Zahariev
 */
public class InvalidPageSizeException extends Exception {
    public InvalidPageSizeException() {super("Page size must be greater than 0");}
}
