package com.example.qlrp.controller;

import com.example.qlrp.entity.Room;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.service.RoomService;
import com.example.qlrp.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/{id}/seats")
    @ResponseBody
    public List<Seat> getSeatsByRoom(@PathVariable int id) {
        Room room = roomService.getRoomById(id);
        // Trả về toàn bộ danh sách ghế của phòng đó
        return room.getSeats();
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }
}