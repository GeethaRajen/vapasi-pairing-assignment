package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.repository.PassengerRepository;
import com.tw.repository.TicketRepository;
import com.tw.util.ResourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Passenger addPassenger(long pnr, Passenger passenger) {

        Ticket ticket = ticketRepository.findById(pnr).orElseThrow(
                () -> new ResourseNotFoundException("Ticket not found with PNR: " + pnr)
        );
        if (ticket.getPassengers().size() >= 10) {
            throw new IllegalStateException("Maximun of 10 passengers per ticket is allowed");
        }
        if (passengerRepository.existsByAadharNumber(passenger.getAadharNumber())) {
            throw new IllegalArgumentException("Aadhar number already exists: " + passenger.getAadharNumber());
        }
        passenger.setTicket(ticket);
        ticket.getPassengers().add(passenger);
        return passengerRepository.save(passenger);
    }

    @Override
    public void removePassenger(long pnr, long pid) {
        Ticket ticket = ticketRepository.findById(pnr).orElseThrow(
                () -> new ResourseNotFoundException("Ticket not found with PNR: " + pnr)
        );

        Passenger passenger = passengerRepository.findById(pid).orElseThrow(
                () -> new ResourseNotFoundException("Passenger not found with ID: " + pid)
        );
        if (!Objects.equals(passenger.getTicket().getPnr(), ticket.getPnr())) {
            throw new IllegalArgumentException("Passenger does not belong to this ticket");
        }
        passengerRepository.delete(passenger);
    }

    @Override
    public Page<Passenger> getAllPassengers(long pnr, int page, int size) {
        Ticket ticket = ticketRepository.findById(pnr).orElseThrow(
                () -> new ResourseNotFoundException("Ticket not found with PNR: " + pnr)
        );
        List<Passenger> passengers = ticket.getPassengers();
        int start = page * size;
        int end = Math.min((start + size), passengers.size());

        List<Passenger> pageList = passengers.subList(start, end);
        return new PageImpl<>(pageList, PageRequest.of(page, size), passengers.size());
    }

}

