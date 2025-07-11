package com.tw.service;

import com.tw.entity.Ticket;
import com.tw.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketByPnr(long pnr) {
        return ticketRepository.findById(pnr).orElseThrow(
                () -> new EntityNotFoundException("Ticket with id " + pnr + " not found")
        );
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void deleteTicket(long pnr) {
        Ticket ticket = getTicketByPnr(pnr);
        ticketRepository.delete(ticket);
    }
}
