package com.example.qlrp.repository;

import com.example.qlrp.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByShowDateOrderByShowTimeAsc(LocalDate showDate);

    @Query("SELECT DISTINCT s.showDate FROM Showtime s WHERE s.movie.movieId = :movieId AND s.showDate >= :today ORDER BY s.showDate ASC")
    List<LocalDate> findAvailableDatesByMovie(@Param("movieId") Integer movieId, @Param("today") LocalDate today);

    @Query("SELECT s FROM Showtime s WHERE s.movie.movieId = :movieId AND s.showDate = :date ORDER BY s.showTime ASC")
    List<Showtime> findShowtimesByMovieAndDate(@Param("movieId") Integer movieId, @Param("date") LocalDate date);
}
