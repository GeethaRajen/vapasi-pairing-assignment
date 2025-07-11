package com.tw.controller;

import com.tw.entity.Ticket;
import com.tw.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<Ticket> getTicketByPnr(@PathVariable long pnr) {
        return new ResponseEntity<>(ticketService.getTicketByPnr(pnr), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @DeleteMapping("/{pnr}")
    public ResponseEntity<String> deleteTicket(@PathVariable long pnr) {
        ticketService.deleteTicket(pnr);
        return new ResponseEntity<>("Ticket with id " + pnr + " deleted", HttpStatus.OK);
    }
}
