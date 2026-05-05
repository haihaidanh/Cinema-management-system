package com.example.qlrp.entity;

import com.example.qlrp.contants.SeatAvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatAvailabilityStatus status; // AVAILABLE, BOOKED

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    @JsonIgnore
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    @JsonIgnore
    private Seat seat;

    @OneToOne(mappedBy = "seatAvailability")
    @JsonIgnore
    private Ticket ticket;
}
