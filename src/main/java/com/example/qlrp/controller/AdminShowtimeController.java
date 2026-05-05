package com.example.qlrp.controller;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.service.MovieService;
import com.example.qlrp.service.RoomService;
import com.example.qlrp.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/showtimes")
public class AdminShowtimeController {
    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MovieService movieService; // Để lấy danh sách phim cho dropdown

    @Autowired
    private RoomService roomService;   // Để lấy danh sách phòng cho dropdown

    @GetMapping
    public String listShowtimes(Model model) {
        model.addAttribute("showtimes", showtimeService.getAllShowtimes());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin-showtimes";
    }

    @PostMapping("/add")
    public String addShowtime(@ModelAttribute Showtime showtime) {
        showtimeService.saveShowtime(showtime);
        return "redirect:/admin/showtimes"; // Quay lại trang danh sách
    }

    @GetMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable Integer id) {
        try {
            showtimeService.deleteShowtime(id);
        } catch (Exception e) {
            System.out.println("Lỗi: Suất chiếu này đã có vé, không thể xóa!");
        }
        return "redirect:/admin/showtimes";
    }
}