package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.exception.DuplicatePassengerException;
import com.tw.exception.PassengerLimitExceededException;
import com.tw.exception.PassengerNotFoundException;
import com.tw.repository.PassengerRepository;
import com.tw.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    @Override
    public Passenger addPassenger(long pnr, Passenger passenger) {

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
        return passengerRepository.save(passenger);
    }

    @Override
    public List<Passenger> getPassengersByAadharTravelDate(String aadhar, LocalDate travelDate) {
        return mediatorService.getPassengersByAadharTravelDate(aadhar, travelDate);
    }

    @Override
    public boolean removePassenger(long pnr, long pid) {

        boolean shouldDeleteTicket = false;
        Ticket ticket = mediatorService.getTicketByPnr(pnr);
        LOGGER.info("Ticket with pnr " + pnr + " ticket : " + ticket);
        if (ticket.getPassengers().size() == AppConstants.MIN_PASSENGERS) {
            shouldDeleteTicket = true;
        }
        Passenger passenger = passengerRepository.findById(pid).orElseThrow(
                () -> new PassengerNotFoundException("Passenger not found with ID: " + pid)
        );
        LOGGER.info("passenger : " + passenger);
        if (!Objects.equals(passenger.getTicket().getPnr(), ticket.getPnr())) {
            LOGGER.info(String.valueOf(Objects.equals(passenger.getTicket().getPnr(), ticket.getPnr())));
            throw new PassengerNotFoundException("Passenger does not belong to the ticket with PNR: " + pnr);
        }
        passengerRepository.deleteById(passenger.getPid());

        LOGGER.info("shouldDeleteTicket ? " + shouldDeleteTicket);
        if (shouldDeleteTicket) {
            mediatorService.deleteTicket(pnr);
        }
        return shouldDeleteTicket;
    }

    @Override
    public Page<Passenger> getAllPassengers(long pnr, int page, int size) {
        Ticket ticket = mediatorService.getTicketByPnr(pnr);
        List<Passenger> passengers = ticket.getPassengers();
        int start = page * size;
        int end = Math.min((start + size), passengers.size());

        List<Passenger> pageList = passengers.subList(start, end);
        return new PageImpl<>(pageList, PageRequest.of(page, size), passengers.size());
    }
}