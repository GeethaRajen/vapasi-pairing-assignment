package com.tw.controller;

import com.tw.entity.Passenger;
import com.tw.service.PassengerService;
import com.tw.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/passengers/v1")
public class PassengerController {
    private static final Logger LOGGER = Logger.getLogger(PassengerController.class.getName());
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping("/{pnr}")
    public ResponseEntity<Passenger> addPassenger(@PathVariable long pnr, @Valid @RequestBody Passenger passenger) {
        LOGGER.info("Adding passenger to ticket " + pnr);
        return new ResponseEntity<>(passengerService.addPassenger(pnr, passenger), HttpStatus.CREATED);
    }

    @DeleteMapping("/{pid}/ticket/{pnr}")
    public ResponseEntity<String> removePassenger(@PathVariable long pnr, @PathVariable long pid) {
        LOGGER.info("Removing passenger from ticket " + pnr);
        boolean isTicketDeleted = passengerService.removePassenger(pnr, pid);

        StringBuilder response = new StringBuilder();
        response.append("Passenger with id ").append(pid).append(" is removed from ticket ").append(pnr);
        if (isTicketDeleted) {
            response.append(System.lineSeparator()).append("Ticket is also deleted.");
        }
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<Page<Passenger>> getAllPassengers(@PathVariable long pnr,
                                                            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NO) int page,
                                                            @RequestParam(defaultValue = AppConstants.DEFAULT_RESULT_COUNT) int size) {
        LOGGER.info("Retrieve all passengers in the ticket " + pnr);
        return new ResponseEntity<>(passengerService.getAllPassengers(pnr, page, size), HttpStatus.OK);
    }
}