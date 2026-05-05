package com.example.qlrp.entity;

import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatTypeId;

    private String typeName;    // NORMAL, VIP, SWEETBOX
    private float surcharge;    // Phụ thu theo loại ghế

    @OneToMany(mappedBy = "seatType")
    @JsonIgnore
    private List<Seat> seats;
}
