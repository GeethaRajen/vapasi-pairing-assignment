package com.tw.service;

import com.tw.entity.Passenger;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PassengerService {

    Passenger addPassenger(Long pnr, Passenger passenger);

    boolean removePassenger(Long pnr, Long pid);

    Page<Passenger> getAllPassengers(Long pnr, int page, int size);

    List<Passenger> getPassengersByAadharTravelDate(String aadhar, LocalDate travelDate);
}
