package com.example.qlrp.service;

import com.example.qlrp.contants.SeatStatus;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public void updateStatus(int seatId, String statusName) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế có ID: " + seatId));

        // Chuyển đổi String từ Frontend gửi lên thành Enum
        try {
            SeatStatus newStatus = SeatStatus.valueOf(statusName.toUpperCase());
            seat.setStatus(newStatus);
            seatRepository.save(seat); // Cập nhật xuống DB
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Trạng thái ghế không hợp lệ: " + statusName);
        }
    }
}