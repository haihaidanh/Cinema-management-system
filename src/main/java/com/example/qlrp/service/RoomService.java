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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatTypeRepository seatTypeRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public void saveRoom(Room room) {

        if (roomRepository.existsByRoomName(room.getRoomName())) {
            throw new RuntimeException("Tên phòng '" + room.getRoomName() + "' đã tồn tại!");
        }

        if (room.getTotalRows() <= 0 || room.getTotalColumns() <= 0) {
            throw new RuntimeException("Số hàng và số cột không được bằng 0!");
        }

        Room savedRoom = roomRepository.save(room);

        // 1. Lấy sẵn SeatTypes
        SeatType normalType = seatTypeRepository.findById(1).orElse(null);
        SeatType vipType = seatTypeRepository.findById(2).orElse(null);
        SeatType sweetboxType = seatTypeRepository.findById(3).orElse(null);

        List<Seat> seats = new ArrayList<>();
        int totalRows = savedRoom.getTotalRows();
        int totalCols = savedRoom.getTotalColumns();

        for (int i = 0; i < totalRows; i++) {
            String rowName = String.valueOf((char) ('A' + i));

            for (int j = 1; j <= totalCols; j++) {
                Seat seat = new Seat();
                seat.setSeatRow(rowName);
                seat.setSeatNumber(j);
                seat.setRoom(savedRoom);

                // --- LOGIC DYNAMIC (ÁP DỤNG CHO MỌI KÍCH THƯỚC) ---

                // 1. Hàng cuối cùng luôn là Sweetbox
                if (i == totalRows - 1) {
                    seat.setSeatType(sweetboxType);
                }
                // 2. Hai cột đầu và hai cột cuối luôn là ghế Thường (vùng biên)
                else if (j <= 2 || j > totalCols - 2) {
                    seat.setSeatType(normalType);
                }
                // 3. Các hàng đầu tiên (khoảng 1/3 phòng phía trên) là ghế Thường
                // Ví dụ: Phòng 10 hàng thì 3 hàng đầu là ghế thường
                else if (i < totalRows / 3) {
                    seat.setSeatType(normalType);
                }
                // 4. Các vị trí trung tâm còn lại là ghế VIP
                else {
                    seat.setSeatType(vipType);
                }

                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }

    @Transactional // Quan trọng: Đảm bảo tính nguyên tử của giao dịch
    public void deleteRoom(Integer roomId) {
        // Bước 1: Xóa tất cả ghế liên quan đến phòng trước
        seatRepository.deleteByRoom_RoomId(roomId);

        // Bước 2: Sau khi "dọn dẹp" sạch ghế, mới tiến hành xóa phòng
        roomRepository.deleteById(roomId);
    }

    public List<SeatDTO> getSeatsByRoom(int roomId) {
        List<Seat> seats = seatRepository.findByRoom_RoomId(roomId);
        return seats.stream().map(s -> {
            SeatDTO dto = new SeatDTO();
            dto.setSeatId(s.getSeatId());
            dto.setSeatRow(s.getSeatRow());
            dto.setSeatNumber(s.getSeatNumber());
            dto.setTypeName(s.getSeatType().getTypeName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateRoom(Integer id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng có ID: " + id));

        // Cập nhật các trường cho phép sửa
        room.setRoomName(roomDetails.getRoomName());
        room.setRoomType(roomDetails.getRoomType());
        room.setDescription(roomDetails.getDescription());

        // Lưu lại vào DB
        roomRepository.save(room);
    }
}
