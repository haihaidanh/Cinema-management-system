package com.example.qlrp.repository;

import com.example.qlrp.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    // JpaRepository đã có sẵn các hàm findById và save
}
