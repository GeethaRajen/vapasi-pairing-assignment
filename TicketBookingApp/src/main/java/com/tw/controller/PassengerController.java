package com.tw.controller;

import com.tw.entity.Passenger;
import com.tw.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping("/{pnr}")
    public ResponseEntity<Passenger> addPassenger(@PathVariable long pnr, @RequestBody Passenger passenger) {
        return new ResponseEntity<>(passengerService.addPassenger(pnr, passenger), HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<String> removePassenger(@PathVariable long pnr, @PathVariable long pid) {
        passengerService.removePassenger(pnr, pid);
        return new ResponseEntity<>("Passenger with id " + pid + " removed", HttpStatus.OK);
    }

    @GetMapping("/all/{pnr}")
    public ResponseEntity<Page<Passenger>> getAllPassengers(@PathVariable long pnr,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(passengerService.getAllPassengers(pnr, page, size), HttpStatus.OK);
    }
}
