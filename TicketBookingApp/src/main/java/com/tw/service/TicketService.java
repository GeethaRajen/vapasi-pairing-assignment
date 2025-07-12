package com.tw.service;

import com.tw.entity.Ticket;

import java.util.List;

public interface TicketService {

    Ticket createTicket(Ticket ticket);

    Ticket getTicketByPnr(long pnr);

    void deleteTicket(long pnr);

    List<Ticket> getAllTicketsByLimit(int offset, int limit);
}