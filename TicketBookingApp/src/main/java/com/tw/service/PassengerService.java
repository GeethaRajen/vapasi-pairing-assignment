package com.tw.service;

import com.tw.entity.Passenger;

public interface PassengerService {

    public Passenger add(long pnr, Passenger passenger);

    public void remove(long pnr, long pid);
}
