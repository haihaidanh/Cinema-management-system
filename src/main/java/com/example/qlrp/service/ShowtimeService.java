package com.example.qlrp.service;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;


    public void saveShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(int id) {
        showtimeRepository.deleteById(id);
    }

    public boolean checkDuplicate(Integer roomId, LocalDate date, LocalTime time, Integer excludeId) {
        // Lấy tất cả suất chiếu của phòng đó trong ngày đó
        List<Showtime> existingShows = showtimeRepository.findByRoom_RoomIdAndShowDate(roomId, date);

        // Giả định mỗi suất chiếu kéo dài 150 phút (2h30p)
        int durationInMinutes = 150;
        LocalTime newStart = time;
        LocalTime newEnd = newStart.plusMinutes(durationInMinutes);

        for (Showtime ex : existingShows) {
            // Nếu là thao tác UPDATE, bỏ qua việc so sánh với chính nó
            if (excludeId != null && ex.getShowtimeId().equals(excludeId)) {
                continue;
            }

            LocalTime exStart = ex.getShowTime();
            LocalTime exEnd = exStart.plusMinutes(durationInMinutes);

            // Thuật toán kiểm tra giao thoa (Overlap):
            // Một suất chiếu bị trùng nếu: (Bắt đầu mới < Kết thúc cũ) VÀ (Kết thúc mới > Bắt đầu cũ)
            if (newStart.isBefore(exEnd) && newEnd.isAfter(exStart)) {
                return true; // Bị trùng lịch
            }
        }
        return false; // Không trùng
    }
    // Ví dụ logic lọc đơn giản trong Service
    public List<Showtime> filterShowtimes(Long movieId, Long roomId, LocalDate date) {
        if (movieId == null && roomId == null && date == null) {
            return showtimeRepository.findAll();
        }
        return showtimeRepository.findWithFilters(movieId, roomId, date);
    }
}
