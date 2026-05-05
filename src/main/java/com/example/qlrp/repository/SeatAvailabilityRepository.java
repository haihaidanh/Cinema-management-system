package com.example.qlrp.repository;

import com.example.qlrp.entity.SeatAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatAvailabilityRepository extends JpaRepository<SeatAvailability, Integer> {
    List<SeatAvailability> findByShowtimeShowtimeId(Integer showtimeId);
}
