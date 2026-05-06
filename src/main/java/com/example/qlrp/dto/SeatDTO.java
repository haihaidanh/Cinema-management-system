package com.example.qlrp.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer seatId;
    private Integer roomId;
    private String seatRow;
    private Integer seatNumber;
    private String typeName;
}