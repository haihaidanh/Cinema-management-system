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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    private String roomName;
    private String roomType;

    private int totalRows;    // Số hàng của ma trận
    private int totalColumns; // Số cột của ma trận

    private String description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Seat> seats;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Showtime> showtimes;

    // Helper để tính tổng số ghế thực tế đã lắp
    public int getActiveSeatsCount() {
        return seats != null ? seats.size() : 0;
    }
}
