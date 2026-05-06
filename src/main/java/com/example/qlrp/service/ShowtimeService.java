package com.example.qlrp.service;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Lấy tất cả suất chiếu
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    // Tìm suất chiếu theo ID
    public Optional<Showtime> findById(int id) {
        return showtimeRepository.findById(id);
    }

    // Lấy danh sách ngày chiếu hợp lệ của bộ phim (từ hôm nay trở đi)
    // Sequence: Lớp Showtime gọi phương thức lấy danh sách ngày chiếu theo movieID
    public List<LocalDate> findAvailableDatesByMovie(int movieId) {
        LocalDate today = LocalDate.now();
        return showtimeRepository.findAvailableDatesByMovie(movieId, today);
    }

    // Tìm suất chiếu theo phim và ngày cụ thể
    // Sequence: Lớp Showtime gọi phương thức tìm kiếm suất chiếu theo ngày
    public List<Showtime> findShowtimesByMovieAndDate(int movieId, LocalDate date) {
        return showtimeRepository.findShowtimesByMovieAndDate(movieId, date);
    }

    public void saveShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(int id) {
        showtimeRepository.deleteById(id);
    }
}
