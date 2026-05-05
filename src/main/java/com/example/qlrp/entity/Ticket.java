package com.example.qlrp.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    private float price;

    @OneToOne
    @JoinColumn(name = "seat_availability_id")
    private SeatAvailability seatAvailability;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
