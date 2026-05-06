package com.example.qlrp.repository;

import com.example.qlrp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomerCustomerIdOrderByBookingTimeDesc(int customerId);
}
