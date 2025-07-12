package com.tw.exception;

public class TicketNotCreatedException extends RuntimeException {
    public TicketNotCreatedException(String message) {
        super(message);
    }
}
