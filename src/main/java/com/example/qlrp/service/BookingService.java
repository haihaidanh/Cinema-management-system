package com.example.qlrp.service;

import com.example.qlrp.entity.*;
import com.example.qlrp.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatAvailabilityService seatAvailabilityService;

    @Autowired
    private TicketService ticketService;

    // Tìm booking theo ID
    public Optional<Booking> findById(int id) {
        return bookingRepository.findById(id);
    }

    // Lấy lịch sử đặt vé của khách hàng (sắp xếp mới nhất trước)
    public List<Booking> findByCustomerId(int customerId) {
        return bookingRepository.findByCustomerCustomerIdOrderByBookingTimeDesc(customerId);
    }

    /**
     * Nghiệp vụ cốt lõi: Tạo đơn đặt vé
     * Tuân thủ Sequence Diagram UC-4.2 Đặt vé:
     * 1. Lớp ConfirmView gọi lớp SeatAvailability yêu cầu cập nhật trạng thái ghế → BOOKED
     * 2. Lớp ConfirmView gọi lớp Booking yêu cầu xử lý lưu hóa đơn đặt vé
     * 3. Lớp ConfirmView gọi lớp Ticket yêu cầu tạo vé điện tử cho từng ghế
     */
    @Transactional
    public Booking createBooking(Customer customer, Showtime showtime, List<SeatAvailability> selectedSeats) {
        // Bước 1: Validate tất cả ghế còn AVAILABLE
        if (!seatAvailabilityService.validateSeatsAvailable(selectedSeats)) {
            throw new RuntimeException("Ghế bạn chọn vừa được khách hàng khác đặt mua. Vui lòng chọn ghế khác.");
        }

        // Bước 2: Cập nhật trạng thái ghế → BOOKED
        // Sequence: Lớp SeatAvailability gọi phương thức cập nhật trạng thái ghế
        seatAvailabilityService.updateStatusToBooked(selectedSeats);

        // Bước 3: Tính tổng tiền
        float totalAmount = seatAvailabilityService.calculateTotalAmount(selectedSeats, showtime.getBasePrice());

        // Bước 4: Tạo Booking (hóa đơn)
        // Sequence: Lớp Booking gọi phương thức tạo hóa đơn
        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);
        booking.setCustomer(customer);
        booking = bookingRepository.save(booking);

        // Bước 5: Tạo Ticket cho từng ghế
        // Sequence: Lớp Ticket gọi phương thức tạo vé (Tính giá = basePrice + surcharge)
        List<Ticket> tickets = ticketService.createTicketsForBooking(booking, showtime, selectedSeats);
        booking.setTickets(tickets);

        return booking;
    }
}
