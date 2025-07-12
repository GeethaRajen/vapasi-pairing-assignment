package com.tw.exception;

public class DuplicatePassengerException extends RuntimeException {
    public DuplicatePassengerException(String message) {
        super(message);
    }
}
