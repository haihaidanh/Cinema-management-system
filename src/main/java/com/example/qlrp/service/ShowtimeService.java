package com.example.qlrp.service;

import com.example.qlrp.contants.SeatAvailabilityStatus;
import com.example.qlrp.entity.Seat;
import com.example.qlrp.entity.SeatAvailability;
import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.SeatAvailabilityRepository;
import com.example.qlrp.repository.SeatRepository;
import com.example.qlrp.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime; // Giữ lại để dùng cho logic checkDuplicate
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    @Autowired
    private SeatRepository seatRepository;

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

    // Giả sử đây là hàm trong ShowtimeService
    public void saveShowtime(Showtime showtime) {
        if (showtime.getShowtimeId() != null) {
            // 1. Tìm đối tượng cũ từ Database
            Showtime existingShowtime = showtimeRepository.findById(showtime.getShowtimeId()).get();

            // 2. Cập nhật các trường cơ bản
            existingShowtime.setMovie(showtime.getMovie());
            existingShowtime.setRoom(showtime.getRoom());
            existingShowtime.setShowDate(showtime.getShowDate());
            existingShowtime.setShowTime(showtime.getShowTime());
            existingShowtime.setBasePrice(showtime.getBasePrice());

            // 3. XỬ LÝ DANH SÁCH SEATS (Quan trọng nhất)
            // Không được dùng: existingShowtime.setSeatAvailabilities(showtime.getSeatAvailabilities());

            existingShowtime.getSeatAvailabilities().clear(); // Xóa nội dung bên trong
            if (showtime.getSeatAvailabilities() != null) {
                existingShowtime.getSeatAvailabilities().addAll(showtime.getSeatAvailabilities()); // Thêm phần tử mới vào list cũ
            }

            showtimeRepository.save(existingShowtime);
        } else {
            // 1. Lấy tất cả các ghế thuộc về phòng chiếu này
            List<Seat> seatsInRoom = seatRepository.findByRoom_RoomId(showtime.getRoom().getRoomId());

            // 2. Tạo danh sách SeatAvailability trống cho suất chiếu mới
            List<SeatAvailability> availabilities = new ArrayList<>();

            for (Seat seat : seatsInRoom) {
                SeatAvailability sa = new SeatAvailability();
                sa.setSeat(seat);
                sa.setShowtime(showtime); // Liên kết với suất chiếu này
                sa.setStatus(SeatAvailabilityStatus.AVAILABLE); // Trạng thái mặc định là còn trống
                availabilities.add(sa);
            }

            // 3. Gán danh sách ghế vào suất chiếu
            showtime.setSeatAvailabilities(availabilities);

            // 4. Lưu Showtime (Nhờ CascadeType.ALL, các SeatAvailability sẽ được lưu theo)
            showtimeRepository.save(showtime);
        }
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

    public boolean hasBookedSeats(int showtimeId) {
        // Tìm suất chiếu
        Optional<Showtime> st = showtimeRepository.findById(showtimeId);
        if (st.isPresent()) {
            // Kiểm tra trong danh sách seatAvailabilities của suất chiếu đó
            return st.get().getSeatAvailabilities().stream()
                    .anyMatch(sa -> sa.getStatus().equals(SeatAvailabilityStatus.BOOKED));
        }
        return false;
    }
}