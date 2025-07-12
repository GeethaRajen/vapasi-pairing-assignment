package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.exception.TicketNotFoundException;
import com.tw.repository.PassengerRepository;
import com.tw.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MediatorServiceImpl implements MediatorService {

    private final PassengerRepository passengerRepository;
    private final TicketRepository ticketRepository;

    public MediatorServiceImpl(PassengerRepository passengerRepository, TicketRepository ticketRepository){
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Passenger> getPassengersByAadharTravelDate(String aadhar, LocalDate travelDate) {
        return passengerRepository.findPassengersByAadharNumberAndTicketTravelDate(aadhar,travelDate);
    }

    @Override
    public void deleteTicket(long pnr) {
        ticketRepository.deleteById(pnr);
    }


    @Override
    public Ticket getTicketByPnr(long pnr) {
        return ticketRepository.findById(pnr).orElseThrow(
                () -> new TicketNotFoundException("Ticket with id " + pnr + " not found")
        );
    }
}
