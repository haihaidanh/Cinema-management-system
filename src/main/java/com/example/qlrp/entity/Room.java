package com.example.qlrp.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    private String roomName;

    private int numberOfSeats;
    private String description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @OneToMany(mappedBy = "room")
    private List<Showtime> showtimes;
}
