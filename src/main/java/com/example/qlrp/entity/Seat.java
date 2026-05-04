package com.example.qlrp.entity;

import com.example.qlrp.contants.SeatStatus;
import com.example.qlrp.contants.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    private Integer seatId;
    private String seatRow;
    private Integer seatNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    private SeatType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;
}
