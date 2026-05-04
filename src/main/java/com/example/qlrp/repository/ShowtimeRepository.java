package com.example.qlrp.repository;

import com.example.qlrp.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByShowDateOrderByShowTimeAsc(LocalDate showDate);
}
