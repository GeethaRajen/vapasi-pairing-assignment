package com.tw.controller;

import com.tw.entity.Ticket;
import com.tw.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tickets/v1")
public class TicketController {
    private static final Logger LOGGER = Logger.getLogger(TicketController.class.getName());

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(value = "/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        LOGGER.info("Received request to create a new ticket");
        return new ResponseEntity<>(ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<Ticket> getTicketByPnr(@PathVariable long pnr) {
        LOGGER.info("Received request to get ticket by pnr " + pnr);
        return new ResponseEntity<>(ticketService.getTicketByPnr(pnr), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Received request to get all tickets with page " + page + " and size " + size);
        return new ResponseEntity<>(ticketService.getAllTicketsByLimit(page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{pnr}")
    public ResponseEntity<String> deleteTicket(@PathVariable long pnr) {
        LOGGER.info("Received request to delete ticket with pnr " + pnr);
        ticketService.deleteTicket(pnr);
        return new ResponseEntity<>("Ticket with id " + pnr + " deleted", HttpStatus.OK);
    }
}