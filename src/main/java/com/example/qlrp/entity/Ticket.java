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

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    private float price;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
