package com.example.qlrp.controller;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.service.MovieService;
import com.example.qlrp.service.RoomService;
import com.example.qlrp.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/showtimes")
public class AdminShowtimeController {
    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private MovieService movieService; // Để lấy danh sách phim cho dropdown

    @Autowired
    private RoomService roomService;   // Để lấy danh sách phòng cho dropdown


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

    @GetMapping("/check-overlap")
    @ResponseBody
    public Map<String, Object> checkOverlap(
            @RequestParam Integer roomId,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam(required = false) Integer id) {

        // Logic kiểm tra trong Database của bạn ở đây
        boolean isOverlapping = showtimeService.checkDuplicate(roomId, LocalDate.parse(date), LocalTime.parse(time), id);

        Map<String, Object> response = new HashMap<>();
        response.put("isOverlapping", isOverlapping);
        return response;
    }
    @GetMapping
    public String listShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        // 1. Lấy danh sách đã lọc từ Service
        List<Showtime> showtimes = showtimeService.filterShowtimes(movieId, roomId, date);

        // 2. Đưa dữ liệu danh sách vào Model
        model.addAttribute("showtimes", showtimes);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("rooms", roomService.getAllRooms());

        // 3. QUAN TRỌNG: Đưa các biến lọc vào Model để giao diện nhận diện được
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("selectedDate", date);

        return "admin-showtimes";
    }
}