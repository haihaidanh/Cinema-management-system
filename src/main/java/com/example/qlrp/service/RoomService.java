package com.example.qlrp.service;

import com.example.qlrp.dto.SeatDTO;
import com.example.qlrp.entity.Room;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.repository.RoomRepository;
import com.example.qlrp.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
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
