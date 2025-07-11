package com.tw.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pnr;

    @Column(length = 20, nullable = false)
    private String source;

    @Column(length = 20, nullable = false)
    private String destination;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @JsonManagedReference("tick")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Passenger> passengers = new ArrayList<>();
}
