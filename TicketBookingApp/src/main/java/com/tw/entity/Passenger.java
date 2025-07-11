package com.tw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private long pid;

    @Column(nullable = false)
    private String name;

    private String gender;

    private int age;

    @Column(name = "aadhaar_number", nullable = false, unique = true, length = 12)
    private String aadharNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @JsonBackReference("tick")
    private Ticket ticket;
}
