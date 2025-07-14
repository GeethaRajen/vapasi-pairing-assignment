package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.exception.DuplicatePassengerException;
import com.tw.exception.PassengerLimitExceededException;
import com.tw.exception.PassengerNotFoundException;
import com.tw.repository.PassengerRepository;
import com.tw.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final Logger LOGGER = Logger.getLogger(PassengerServiceImpl.class.getName());

    private final PassengerRepository passengerRepository;
    private MediatorService mediatorService;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Autowired
    public void setPassengerService(MediatorService mediatorService) {
        this.mediatorService = mediatorService;
    }

    @Transactional
    @Override
    public Passenger addPassenger(Long pnr, Passenger passenger) {
        LOGGER.log(Level.FINE,"Adding passenger...");
        Ticket ticket = mediatorService.getTicketByPnr(pnr);
        List<Passenger> passengersList = ticket.getPassengers();
        if (passengersList.size() >= AppConstants.MAX_PASSENGERS) {
            throw new PassengerLimitExceededException("Maximum passengers allowed per ticket is " + AppConstants.MAX_PASSENGERS);
        }

        if (ticket.getPassengers().contains(passenger)) {
            throw new DuplicatePassengerException("Passenger already exist for ticket " + pnr);
        }

        List<Passenger> samePassengerList = getPassengersByAadharTravelDate(passenger.getAadharNumber(), ticket.getTravelDate());
        if (!samePassengerList.isEmpty()) {
            throw new IllegalStateException("Passenger cannot book more than 1 ticket on the same travel date");
        }

        passenger.setTicket(ticket);
        Passenger createdPassenger = passengerRepository.save(passenger);
        LOGGER.log(Level.FINE,"Passenger created successfully with id "+createdPassenger.getPid());
        return createdPassenger;
    }

    @Override
    public List<Passenger> getPassengersByAadharTravelDate(String aadhar, LocalDate travelDate) {
        return mediatorService.getPassengersByAadharTravelDate(aadhar, travelDate);
    }

    @Transactional
    @Override
    public boolean removePassenger(Long pnr, Long passengerId) {

        boolean shouldDeleteTicket = false;
        boolean isPassengerPresent = false;
        Passenger removePassenger = null;

        List<Passenger> passengers = passengerRepository.findPassengersByTicket_Pnr(pnr);
        if (passengers.size() == AppConstants.MIN_PASSENGERS) {
            shouldDeleteTicket = true;
        }

        for(Passenger existingPassenger: passengers) {
            if(existingPassenger.getPid() == passengerId){
                isPassengerPresent = true;
                removePassenger = existingPassenger;
            }
        }
        if(!isPassengerPresent) {
            throw new PassengerNotFoundException("Passenger not found with ID: " + passengerId);
        }

        LOGGER.log(Level.FINE,"Passenger to be deleted: " + removePassenger);
        passengerRepository.delete(removePassenger);

        if (shouldDeleteTicket) {
            mediatorService.deleteTicket(pnr);
            LOGGER.log(Level.FINE,"Ticket deleted successfully");
        }
        return shouldDeleteTicket;
    }

    @Override
    public Page<Passenger> getAllPassengers(Long pnr, int page, int size) {
         Pageable pageable = PageRequest.of(page, size);
         return passengerRepository.findPassengersByTicket_Pnr(pnr, pageable);
    }
}