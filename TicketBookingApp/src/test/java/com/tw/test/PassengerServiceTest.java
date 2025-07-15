package com.tw.test;

import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.repository.PassengerRepository;
import com.tw.repository.TicketRepository;
import com.tw.service.MediatorServiceImpl;
import com.tw.service.PassengerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PassengerServiceTest {
    @Mock
    private PassengerRepository repository;

    @Mock
    private MediatorServiceImpl mediatorService;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private PassengerServiceImpl service;

    private Ticket ticket;
    private Passenger passenger;

    @BeforeEach
    public void setup() {
        //Data setup
        passenger = Passenger.builder().pid(123L)
                .name("Prem").gender("Male")
                .age(20).aadharNumber("987654321987").build();
        List<Passenger> passengerList = new ArrayList<>();
        passengerList.add(passenger);

        ticket = Ticket.builder().pnr(100L).source("Chennai")
                .destination("Bangalore")
                .travelDate(LocalDate.of(2025, 7, 20))
                .passengers(passengerList)
                .build();
        passenger.setTicket(ticket);
    }

    @Test
    public void add_validPassenger_returnsPassenger() {
        long pnr = 100L;
        Passenger passenger = Passenger.builder().pid(888L)
                .name("Nikitha").gender("Female")
                .age(20).aadharNumber("987654321988").build();
        passenger.setTicket(ticket);
        when(ticketRepository.findById(pnr)).thenReturn(Optional.of(ticket));
        when(mediatorService.getTicketByPnr(pnr)).thenReturn(ticket);
        when(repository.save(passenger)).thenReturn(passenger);

        Passenger savedPassenger = service.addPassenger(pnr, passenger);

        Assertions.assertNotNull(savedPassenger);
    }

    @Test
    public void delete_validPassenger_deletesAsExpected() {

        setup();
        // precondition
        willDoNothing().given(repository).delete(passenger);
        when(repository.findPassengersByTicket_Pnr(Mockito.any())).thenReturn(ticket.getPassengers());

        // action
        service.removePassenger(ticket.getPnr(), passenger.getPid());

        // verify
        verify(repository, times(1)).delete(passenger);
    }

}
