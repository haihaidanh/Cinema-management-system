package com.example.qlrp.entity;

import com.example.qlrp.contants.RoomType;
import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    private String roomName;
    @Enumerated(EnumType.STRING) // Lưu dưới dạng chữ (VD: 'STANDARD', 'VIP')
    @Column(name = "room_type", length = 20) // Giới hạn độ dài cột trong DB
    private RoomType roomType;

    private int totalRows;    // Số hàng của ma trận
    private int totalColumns; // Số cột của ma trận

    private String description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Seat> seats;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    @ToString.Exclude
    private List<Showtime> showtimes;

    // Helper để tính tổng số ghế thực tế đã lắp
    public int getActiveSeatsCount() {
        return seats != null ? seats.size() : 0;
    }
}
