package com.tw.service;

import com.tw.entity.Ticket;

import java.util.List;

public interface TicketService {

    public Ticket createTicket(Ticket ticket);

    public Ticket getTicketByPnr(long pnr);

    public List<Ticket> getAllTickets();

    public void deleteTicket(long pnr);
}
