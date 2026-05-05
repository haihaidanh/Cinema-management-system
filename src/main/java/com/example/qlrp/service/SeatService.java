package com.example.qlrp.service;

import com.example.qlrp.entity.Seat;
import com.example.qlrp.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public Seat getSeatById(int seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế có ID: " + seatId));
    }

    public void saveSeat(Seat seat) {
        seatRepository.save(seat);
    }
}