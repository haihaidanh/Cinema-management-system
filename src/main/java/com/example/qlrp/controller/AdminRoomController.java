package com.example.qlrp.controller;

import com.example.qlrp.dto.SeatDTO;
import com.example.qlrp.entity.Room;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.service.RoomService;
import com.example.qlrp.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatService seatService;

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin-rooms"; // File html hiển thị danh sách
    }

    @PostMapping("/add")
    @ResponseBody
    public String saveRoom(@RequestBody Room room) {
        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/{roomId}/seats")
    @ResponseBody
    public ResponseEntity<List<SeatDTO>> getRoomSeats(@PathVariable int roomId) {
        List<SeatDTO> seats = roomService.getSeatsByRoom(roomId);
        return ResponseEntity.ok(seats);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: Không thể xóa phòng do có dữ liệu liên quan.");
        }
    }

    @PostMapping("/add-seat")
    public ResponseEntity<?> addSeat(@RequestBody SeatDTO seatDTO) {
        try {
            log.info(seatDTO.toString());
            seatService.addSeat(seatDTO);
            return ResponseEntity.ok("Thêm ghế thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateRoom(@PathVariable Integer id, @RequestBody Room roomDetails) {
        try {
            // Gọi service để cập nhật
            roomService.updateRoom(id, roomDetails);
            return ResponseEntity.ok("Cập nhật phòng thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }
}