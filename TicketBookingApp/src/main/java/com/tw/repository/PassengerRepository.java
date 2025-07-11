package com.tw.repository;

import com.tw.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    boolean existByAadharNumber(String aadharNumber);
}
