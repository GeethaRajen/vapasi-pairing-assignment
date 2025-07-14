package com.tw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private long pid;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String gender;

    private int age;

    @NotBlank
    @Size(min = 12, max = 12, message = "Aadhaar Number should be of length 12")
    @Column(name = "aadhaar_number", nullable = false, length = 12)
    private String aadharNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @JsonBackReference("tick")
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(aadharNumber, passenger.aadharNumber);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", aadharNumber='" + aadharNumber + '\'' +
                '}';
    }

}