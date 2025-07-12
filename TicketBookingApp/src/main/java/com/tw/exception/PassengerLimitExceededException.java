package com.tw.exception;

public class PassengerLimitExceededException extends RuntimeException {
    public PassengerLimitExceededException(String message) {
        super(message);
    }
}
