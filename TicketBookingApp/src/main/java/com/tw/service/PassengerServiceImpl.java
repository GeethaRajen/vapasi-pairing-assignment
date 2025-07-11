package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketService ticketService;

    @Override
    public Passenger add(long pnr, Passenger passenger) {
        if(passengerRepository.existByAadharNumber(passenger.getAadharNumber())){
            throw new IllegalArgumentException("Aadhar number already exists: "+passenger.getAadharNumber());
        }
        Ticket ticket=ticketService.get(pnr);

        if(ticket.getPassengers().size()>=10){
            throw new IllegalStateException("Cannot add more than 10 passengers to a ticket");
        }
        ticket.addPassenger(passenger);

        return passengerRepository.save(passenger);
    }

    @Override
    public void remove(long pnr, long pid) {
        Ticket ticket = ticketService.get(pnr);
        Passenger passenger = passengerRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + pid));

        ticket.removePassenger(passenger);
        passengerRepository.delete(passenger);
    }

}

