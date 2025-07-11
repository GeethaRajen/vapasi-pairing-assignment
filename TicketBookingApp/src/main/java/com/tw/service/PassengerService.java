package com.tw.service;

import com.tw.entity.Passenger;
import org.springframework.data.domain.Page;

public interface PassengerService {

    public Passenger addPassenger(long pnr, Passenger passenger);

    public void removePassenger(long pnr, long pid);

    public Page<Passenger> getAllPassengers(long pnr, int page, int size);
}
