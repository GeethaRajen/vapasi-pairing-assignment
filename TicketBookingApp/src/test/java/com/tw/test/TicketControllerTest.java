package com.tw.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.controller.TicketController;
import com.tw.entity.Passenger;
import com.tw.entity.Ticket;
import com.tw.service.TicketService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TicketController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    private Ticket ticket1, ticket2;

    @BeforeEach
    public void setup() {
        Passenger passenger = new Passenger();
        passenger.setPid(1L);
        passenger.setName("John Doe");
        passenger.setGender("M");
        passenger.setAge(30);
        passenger.setAadharNumber("123456789012");
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);
        ticket1 = new Ticket(123, "Pune", "Delhi", LocalDate.now(), passengers);
        ticket2 = new Ticket(124, "Delhi", "Pune", LocalDate.now().plusDays(5), passengers);
        passenger.setTicket(ticket1);
    }

    @Test
    @Order(1)
    public void testCreateTicket() throws Exception {
        given(ticketService.createTicket(any(Ticket.class))).willReturn(ticket1);
        ResultActions response = mockMvc.perform(post("/tickets/v1/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket1)));
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.source", is("Pune")))
                .andExpect(jsonPath("$.destination", is("Delhi")));
    }

    @Test
    @Order(2)
    public void testGetTicketByPnr() throws Exception {
        given(ticketService.getTicketByPnr(123L)).willReturn(ticket1);
        ResultActions response = mockMvc.perform(get("/tickets/v1/123"));
        response.andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testGetAllTickets() throws Exception {
        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);
        given(ticketService.getAllTicketsByLimit(0, 10)).willReturn(tickets);
        ResultActions response = mockMvc.perform(get("/tickets/v1/all?limit=0&offset=10"));
        response.andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testDeleteTicket() throws Exception {
        willDoNothing().given(ticketService).deleteTicket(123L);
        ResultActions response = mockMvc.perform(delete("/tickets/v1/123"));
        response.andExpect(status().isOk());
        verify(ticketService).deleteTicket(123L);
    }
}
