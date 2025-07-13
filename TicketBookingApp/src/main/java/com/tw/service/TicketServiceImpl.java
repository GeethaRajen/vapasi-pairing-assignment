package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.exception.TicketNotCreatedException;
import com.tw.repository.TicketRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MediatorService mediatorService;

    public TicketServiceImpl(TicketRepository ticketRepository, MediatorService mediatorService) {
        this.ticketRepository = ticketRepository;
        this.mediatorService = mediatorService;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        List<Passenger> passengersList = ticket.getPassengers();
        for (Passenger passenger : passengersList) {
            List<Passenger> duplicateList = mediatorService.getPassengersByAadharTravelDate(passenger.getAadharNumber(), ticket.getTravelDate());
            if (!duplicateList.isEmpty()) {
                throw new TicketNotCreatedException("Cannot book ticket for passenger " + passenger.getName()
                        + " as ticket already exists for the given date! ");
            }
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketByPnr(long pnr) {
        return mediatorService.getTicketByPnr(pnr);
    }

    @Override
    public void deleteTicket(long pnr) {

        mediatorService.deleteTicket(pnr);
    }

    @Override
    public List<Ticket> getAllTicketsByLimit(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return ticketRepository.findAll(pageable).getContent();
    }
}