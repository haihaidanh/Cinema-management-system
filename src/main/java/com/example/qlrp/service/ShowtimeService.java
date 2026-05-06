package com.example.qlrp.service;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime; // Giữ lại để dùng cho logic checkDuplicate
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Lấy tất cả suất chiếu (Dùng cho danh sách Admin)
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    // Tìm suất chiếu theo ID
    public Optional<Showtime> findById(int id) {
        return showtimeRepository.findById(id);
    }

    // Lấy danh sách ngày chiếu hợp lệ của bộ phim (từ hôm nay trở đi)
    public List<LocalDate> findAvailableDatesByMovie(int movieId) {
        LocalDate today = LocalDate.now();
        return showtimeRepository.findAvailableDatesByMovie(movieId, today);
    }

    // Tìm suất chiếu theo phim và ngày cụ thể
    public List<Showtime> findShowtimesByMovieAndDate(int movieId, LocalDate date) {
        return showtimeRepository.findShowtimesByMovieAndDate(movieId, date);
    }

    public void saveShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(int id) {
        showtimeRepository.deleteById(id);
    }

    /**
     * Kiểm tra trùng lịch chiếu trong một phòng
     * Thuật toán Overlap: (Bắt đầu mới < Kết thúc cũ) AND (Kết thúc mới > Bắt đầu cũ)
     */
    public boolean checkDuplicate(Integer roomId, LocalDate date, LocalTime time, Integer excludeId) {
        List<Showtime> existingShows = showtimeRepository.findByRoom_RoomIdAndShowDate(roomId, date);

        int durationInMinutes = 150; // Giả định phim dài 2h30p bao gồm cả dọn phòng
        LocalTime newStart = time;
        LocalTime newEnd = newStart.plusMinutes(durationInMinutes);

        for (Showtime ex : existingShows) {
            if (excludeId != null && ex.getShowtimeId().equals(excludeId)) {
                continue;
            }

            LocalTime exStart = ex.getShowTime();
            LocalTime exEnd = exStart.plusMinutes(durationInMinutes);

            if (newStart.isBefore(exEnd) && newEnd.isAfter(exStart)) {
                return true;
            }
        }
        return false;
    }

    // Logic lọc nâng cao cho Admin
    public List<Showtime> filterShowtimes(Long movieId, Long roomId, LocalDate date) {
        if (movieId == null && roomId == null && date == null) {
            return showtimeRepository.findAll();
        }
        return showtimeRepository.findWithFilters(movieId, roomId, date);
    }
}