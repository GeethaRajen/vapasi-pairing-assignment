package com.tw.repository;

import com.tw.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findPassengersByTicket_Pnr(Long pnr);

    Page<Passenger> findPassengersByTicket_Pnr(Long ticketPnr, Pageable pageable);

    List<Passenger> findPassengersByAadharNumberAndTicketTravelDate(String aadharNumber, LocalDate travelDate);
}
