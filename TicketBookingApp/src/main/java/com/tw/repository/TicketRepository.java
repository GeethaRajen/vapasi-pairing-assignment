package com.tw.repository;

import com.tw.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Long pnr(long pnr);
}
