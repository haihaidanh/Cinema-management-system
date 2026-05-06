package com.example.qlrp.controller;

import com.example.qlrp.entity.Booking;
import com.example.qlrp.entity.Customer;
import com.example.qlrp.entity.SeatAvailability;
import com.example.qlrp.entity.Showtime;
import com.example.qlrp.service.BookingService;
import com.example.qlrp.service.SeatAvailabilityService;
import com.example.qlrp.service.ShowtimeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CustomerBookingController {

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private SeatAvailabilityService seatAvailabilityService;

    @Autowired
    private BookingService bookingService;

    /**
     * Trang xác nhận đơn hàng — ConfirmView
     * Sequence: Lớp ConfirmView hiển thị giao diện xác nhận đơn hàng cho Customer.
     */
    @PostMapping("/bookings/confirm")
    public String showConfirmPage(@RequestParam("showtimeId") Integer showtimeId,
                                  @RequestParam("seatIds") String seatIdsStr,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        Optional<Showtime> showtimeOpt = showtimeService.findById(showtimeId);
        if (showtimeOpt.isEmpty() || seatIdsStr == null || seatIdsStr.isEmpty()) {
            return "redirect:/";
        }

        Showtime showtime = showtimeOpt.get();
        List<Integer> seatIds = Arrays.stream(seatIdsStr.split(","))
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());

        List<SeatAvailability> selectedSeats = seatAvailabilityService.findAllByIds(seatIds);

        // Validate: kiểm tra ghế còn trống
        if (!seatAvailabilityService.validateSeatsAvailable(selectedSeats)) {
            redirectAttributes.addFlashAttribute("error", "Ghế bạn chọn vừa được khách hàng khác đặt mua. Vui lòng chọn ghế khác.");
            return "redirect:/showtimes/" + showtimeId + "/seats";
        }

        // Tính tổng tiền
        float totalAmount = seatAvailabilityService.calculateTotalAmount(selectedSeats, showtime.getBasePrice());

        model.addAttribute("showtime", showtime);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("seatIdsStr", seatIdsStr);

        return "booking-confirm";
    }

    /**
     * Xử lý thanh toán — UC: Thanh toán
     * Sequence:
     * 1. Lớp ConfirmView gọi SeatAvailability cập nhật trạng thái ghế → BOOKED
     * 2. Lớp ConfirmView gọi Booking tạo hóa đơn
     * 3. Lớp ConfirmView gọi Ticket tạo vé điện tử cho từng ghế
     */
    @PostMapping("/bookings/checkout")
    public String checkout(@RequestParam("showtimeId") Integer showtimeId,
                           @RequestParam("seatIds") String seatIdsStr,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Optional<Showtime> showtimeOpt = showtimeService.findById(showtimeId);
        if (showtimeOpt.isEmpty() || seatIdsStr == null || seatIdsStr.isEmpty()) {
            return "redirect:/";
        }

        Showtime showtime = showtimeOpt.get();
        List<Integer> seatIds = Arrays.stream(seatIdsStr.split(","))
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());

        List<SeatAvailability> selectedSeats = seatAvailabilityService.findAllByIds(seatIds);

        // Lấy Customer từ session
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        if (customer == null) {
            return "redirect:/auth/login";
        }

        try {
            // Gọi BookingService xử lý toàn bộ nghiệp vụ đặt vé
            Booking booking = bookingService.createBooking(customer, showtime, selectedSeats);
            return "redirect:/bookings/success/" + booking.getBookingId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/showtimes/" + showtimeId + "/seats";
        }
    }

    /**
     * Trang đặt vé thành công — BookingSuccessView
     * Sequence: Lớp BookingSuccessView hiển thị thông báo đặt vé thành công và thông tin vé điện tử.
     */
    @GetMapping("/bookings/success/{id}")
    public String showSuccessPage(@PathVariable("id") Integer id, Model model) {
        Optional<Booking> bookingOpt = bookingService.findById(id);
        if (bookingOpt.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("booking", bookingOpt.get());
        return "booking-success";
    }

    /**
     * Lịch sử đặt vé — BookingHistoryView / BookingDetailView
     * UC: Xem vé — cho phép khách hàng xem lại chi tiết vé đã đặt.
     */
    @GetMapping("/bookings/history")
    public String showBookingHistory(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        if (customer == null) {
            return "redirect:/auth/login";
        }

        // Lấy tất cả booking của customer (đã sắp xếp mới nhất trước)
        List<Booking> bookings = bookingService.findByCustomerId(customer.getCustomerId());
        model.addAttribute("bookings", bookings);
        return "booking-history";
    }
}
