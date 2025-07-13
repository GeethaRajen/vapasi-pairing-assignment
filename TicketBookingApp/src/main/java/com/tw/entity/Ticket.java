package com.tw.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tw.util.AppConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pnr;

    @NotBlank
    @Column(length = 20, nullable = false)
    private String source;

    @NotBlank
    @Column(length = 20, nullable = false)
    private String destination;

    @NotNull
    @FutureOrPresent
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @NotEmpty(message = "At least one passenger should be added to the ticket")
    @Size(max = AppConstants.MAX_PASSENGERS,
            message = "A ticket can have only " + AppConstants.MAX_PASSENGERS + " passengers")
    @JsonManagedReference("tick")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Passenger> passengers;
}