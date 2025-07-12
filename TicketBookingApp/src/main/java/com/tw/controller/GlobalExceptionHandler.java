package com.tw.controller;

import com.tw.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());
    private static final String ERROR_MSG = "An error occurred in Ticket Booking System";

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleTicketNotFoundException(TicketNotFoundException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PassengerLimitExceededException.class)
    public ResponseEntity<String> handlePassengerLimitExceededException(PassengerLimitExceededException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<String> handlePassengerNotFoundException(PassengerNotFoundException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatePassengerException.class)
    public ResponseEntity<String> handleDuplicatePassengerException(DuplicatePassengerException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketNotCreatedException.class)
    public ResponseEntity<String> handleTicketNotCreatedException(TicketNotCreatedException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.log(Level.SEVERE, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object[]> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.log(Level.WARNING, ERROR_MSG, ex);
        return new ResponseEntity<>(ex.getDetailMessageArguments(), HttpStatus.BAD_REQUEST);
    }
}