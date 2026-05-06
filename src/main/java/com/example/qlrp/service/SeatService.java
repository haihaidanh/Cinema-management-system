package com.example.qlrp.service;

import com.example.qlrp.dto.SeatDTO;
import com.example.qlrp.entity.Room;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.entity.SeatType;
import com.example.qlrp.repository.RoomRepository;
import com.example.qlrp.repository.SeatRepository;
import com.example.qlrp.repository.SeatTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    public Seat getSeatById(int seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế có ID: " + seatId));
    }

    public void saveSeat(Seat seat) {
        seatRepository.save(seat);
    }

    @Transactional
    public void addSeat(SeatDTO seatDTO) {
        try {
            // 1. Tìm Room (Lấy từ DB để Hibernate quản lý entity này)
            Room room = roomRepository.findById(seatDTO.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            // 2. Tìm SeatType (Lấy từ DB dựa trên typeName)
            SeatType seatType = seatTypeRepository.findByTypeName(seatDTO.getTypeName())
                    .orElseThrow(() -> new RuntimeException("SeatType not found"));

            Seat seat = new Seat();
            seat.setSeatRow(seatDTO.getSeatRow());
            seat.setSeatNumber(seatDTO.getSeatNumber());
            seat.setRoom(room);
            seat.setSeatType(seatType);

            // Lưu và hứng kết quả
            Seat savedSeat = seatRepository.save(seat);

            if (savedSeat.getSeatId() != null) {
                System.out.println("Lưu thành công! ID ghế mới: " + savedSeat.getSeatId());
            }
        } catch (Exception e) {
            // Nếu vào đây là thất bại (lỗi 500 bạn đang gặp thường nằm ở đây)
            System.err.println("Lưu thất bại: " + e.getMessage());
            throw e; // Ném tiếp để Controller bắt được
        }
    }
}