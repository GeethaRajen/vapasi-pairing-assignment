package com.tw.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.controller.PassengerController;
import com.tw.entity.Passenger;
import com.tw.service.PassengerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PassengerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PassengerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PassengerService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void add_validPassenger_shouldAdd() throws Exception {
        Passenger passenger = Passenger.builder().pid(123L)
                .name("Prem").gender("Male")
                .age(20).aadharNumber("987654321987").build();

        // precondition
        given(service.addPassenger(any(Long.class), any(Passenger.class))).willReturn(passenger);
        // action
        ResultActions response = mockMvc.perform(post("/passengers/v1/{pnr}",100L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passenger)));
        // verify
        response.andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void add_emptyNameForPassenger_throwsException() throws Exception {
        Passenger passenger = Passenger.builder().pid(123L)
                .gender("Male")
                .age(20).aadharNumber("987654321987").build();

        // precondition
        given(service.addPassenger(any(Long.class), any(Passenger.class))).willReturn(passenger);
        // action
        ResultActions response = mockMvc.perform(post("/passengers/v1/{pnr}",100L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passenger)));
        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException())
                );
    }

    @Test
    @Order(3)
    public void add_EmptyAadhaarForPassenger_throwsException() throws Exception {
        Passenger passenger = Passenger.builder().pid(123L)
                .name("Prem").gender("Male")
                .age(20).aadharNumber(" ").build();

        // precondition
        given(service.addPassenger(any(Long.class), any(Passenger.class))).willReturn(passenger);
        // action
        ResultActions response = mockMvc.perform(post("/passengers/v1/{pnr}",100L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passenger)));
        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException())
                );
    }

    @Test
    @Order(4)
    public void add_NullAadhaarForPassenger_throwsException() throws Exception {
        Passenger passenger = Passenger.builder().pid(123L)
                .name("Prem").gender("Male")
                .age(20).build();

        // precondition
        given(service.addPassenger(any(Long.class), any(Passenger.class))).willReturn(passenger);
        // action
        ResultActions response = mockMvc.perform(post("/passengers/v1/{pnr}",100L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passenger)));
        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException())
                );
    }

   /* @Test
    @Order(5)
    public void delete_givenPnrPid_shouldRemove() throws Exception {
        // precondition
        willDoNothing().given(service).removePassenger(100L, 123L);

        // action
        ResultActions response = mockMvc.perform(delete("/passengers/v1/{pid}/ticket/{pnr}", 123L, 100L));

        // verify
        response.andExpect(status().isOk());
        verify(service).removePassenger(100L, 123L);
    }*/

    @Test
    @Order(6)
    public void get_givenPnr_returnsPassengers() throws Exception {
        Passenger passenger = Passenger.builder().pid(123L)
                .name("Prem").gender("Male")
                .age(20).build();
        List<Passenger> passengers = Collections.singletonList(passenger);
        Page<Passenger> passengerPage = new PageImpl<>(passengers);

        given(service.getAllPassengers(100L, 0, 10)).willReturn(passengerPage);
        ResultActions response = mockMvc.perform(get("/passengers/v1/{pnr}",100L));
        response.andExpect(status().isOk());
    }
}