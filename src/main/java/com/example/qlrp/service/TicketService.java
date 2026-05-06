package com.example.qlrp.service;

import com.example.qlrp.entity.Booking;
import com.example.qlrp.entity.Ticket;
import com.example.qlrp.entity.SeatAvailability;
import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Tạo vé điện tử cho từng ghế trong đơn hàng.
     * Mỗi vé có mã riêng (ticketCode), giá = basePrice + surcharge.
     * Sequence: Lớp Ticket gọi phương thức tạo vé (Tính giá = basePrice + surcharge theo loại ghế).
     */
    public List<Ticket> createTicketsForBooking(Booking booking, Showtime showtime, List<SeatAvailability> seats) {
        List<Ticket> tickets = new ArrayList<>();

        // Lấy số lượng ticket hiện có để tạo mã vé tuần tự
        long currentCount = ticketRepository.count();

        for (int i = 0; i < seats.size(); i++) {
            SeatAvailability sa = seats.get(i);
            float surcharge = (sa.getSeat().getSeatType() != null) ? sa.getSeat().getSeatType().getSurcharge() : 0;

            Ticket ticket = new Ticket();
            // Sinh mã vé duy nhất: TK-00001, TK-00002, ...
            ticket.setTicketCode(String.format("TK-%05d", currentCount + i + 1));
            ticket.setPrice(showtime.getBasePrice() + surcharge);
            ticket.setSeatAvailability(sa);
            ticket.setBooking(booking);
            tickets.add(ticket);
        }

        return ticketRepository.saveAll(tickets);
    }
}
