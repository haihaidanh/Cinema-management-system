package com.example.qlrp.controller;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.entity.SeatAvailability;
import com.example.qlrp.entity.SeatType;
import com.example.qlrp.entity.Showtime;
import com.example.qlrp.service.MovieService;
import com.example.qlrp.service.SeatAvailabilityService;
import com.example.qlrp.service.SeatTypeService;
import com.example.qlrp.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerMovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private SeatAvailabilityService seatAvailabilityService;

    @Autowired
    private SeatTypeService seatTypeService;

    /**
     * UC: Xem chi tiết phim — MovieDetailView
     * Customer click chức năng đặt vé từ lớp MovieDetailView.
     */
    @GetMapping("/movies/{id}")
    public String showMovieDetail(@PathVariable("id") Integer id, Model model) {
        Optional<Movie> movieOpt = movieService.findById(id);
        if (movieOpt.isPresent()) {
            model.addAttribute("movie", movieOpt.get());
            return "movie-detail";
        }
        return "redirect:/";
    }

    /**
     * UC: Chọn suất chiếu — ShowtimeView
     * Sequence:
     * - Lớp ShowtimeView gọi lớp Showtime yêu cầu lấy danh sách ngày chiếu hợp lệ
     * - Customer chọn ngày → ShowtimeView gọi Showtime tìm kiếm suất chiếu theo ngày
     */
    @GetMapping("/movies/{id}/showtimes")
    public String showShowtimes(@PathVariable("id") Integer id,
                                @RequestParam(value = "date", required = false) String dateStr,
                                Model model) {
        Optional<Movie> movieOpt = movieService.findById(id);
        if (movieOpt.isEmpty()) {
            return "redirect:/";
        }

        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);

        // Lớp Showtime trả kết quả danh sách ngày chiếu cho ShowtimeView
        List<LocalDate> availableDates = showtimeService.findAvailableDatesByMovie(id);
        model.addAttribute("availableDates", availableDates);

        if (!availableDates.isEmpty()) {
            LocalDate selectedDate;
            if (dateStr != null && !dateStr.isEmpty()) {
                selectedDate = LocalDate.parse(dateStr);
            } else {
                selectedDate = availableDates.get(0); // Mặc định chọn ngày đầu tiên
            }
            model.addAttribute("selectedDate", selectedDate);

            // Lớp Showtime trả kết quả danh sách suất chiếu cho ShowtimeView
            List<Showtime> showtimes = showtimeService.findShowtimesByMovieAndDate(id, selectedDate);
            model.addAttribute("showtimes", showtimes);
        }

        return "showtime";
    }

    /**
     * UC: Chọn ghế — SelectSeatView
     * Sequence:
     * - Lớp SelectSeatView gọi lớp SeatAvailability lấy ghế theo suất chiếu
     * - Lớp SelectSeatView gọi lớp SeatType lấy thông tin phụ thu
     */
    @GetMapping("/showtimes/{id}/seats")
    public String showSeatSelection(@PathVariable("id") Integer id, Model model) {
        Optional<Showtime> showtimeOpt = showtimeService.findById(id);
        if (showtimeOpt.isEmpty()) {
            return "redirect:/";
        }

        Showtime showtime = showtimeOpt.get();
        model.addAttribute("showtime", showtime);

        // Lớp SeatAvailability trả kết quả cho SelectSeatView
        List<SeatAvailability> seats = seatAvailabilityService.findByShowtimeId(id);
        model.addAttribute("seats", seats);

        // Lớp SeatType trả kết quả cho SelectSeatView
        List<SeatType> seatTypes = seatTypeService.getAllSeatTypes();
        model.addAttribute("seatTypes", seatTypes);

        return "seat-selection";
    }
}
