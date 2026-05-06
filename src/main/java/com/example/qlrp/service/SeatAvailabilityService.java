package com.example.qlrp.service;

import com.example.qlrp.contants.SeatAvailabilityStatus;
import com.example.qlrp.entity.SeatAvailability;
import com.example.qlrp.repository.SeatAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatAvailabilityService {

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    // Lấy danh sách ghế theo suất chiếu
    // Sequence: Lớp SeatAvailability gọi phương thức lấy danh sách ghế theo suất chiếu
    public List<SeatAvailability> findByShowtimeId(int showtimeId) {
        return seatAvailabilityRepository.findByShowtimeShowtimeId(showtimeId);
    }

    // Lấy danh sách ghế theo ID
    public List<SeatAvailability> findAllByIds(List<Integer> ids) {
        return seatAvailabilityRepository.findAllById(ids);
    }

    // Kiểm tra tất cả ghế còn trống
    public boolean validateSeatsAvailable(List<SeatAvailability> seats) {
        for (SeatAvailability sa : seats) {
            if (sa.getStatus() != SeatAvailabilityStatus.AVAILABLE) {
                return false;
            }
        }
        return true;
    }

    // Cập nhật trạng thái ghế thành BOOKED
    // Sequence: Lớp SeatAvailability gọi phương thức cập nhật trạng thái ghế
    public void updateStatusToBooked(List<SeatAvailability> seats) {
        for (SeatAvailability sa : seats) {
            sa.setStatus(SeatAvailabilityStatus.BOOKED);
        }
        seatAvailabilityRepository.saveAll(seats);
    }

    // Tính tổng tiền cho danh sách ghế đã chọn
    public float calculateTotalAmount(List<SeatAvailability> seats, float basePrice) {
        float total = 0;
        for (SeatAvailability sa : seats) {
            float surcharge = (sa.getSeat().getSeatType() != null) ? sa.getSeat().getSeatType().getSurcharge() : 0;
            total += basePrice + surcharge;
        }
        return total;
    }
}
