package com.tw.service;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface MediatorService {
    List<Passenger> getPassengersByAadharTravelDate(String aadhar, LocalDate travelDate);

    Ticket getTicketByPnr(long pnr);

    void deleteTicket(long pnr);
}
