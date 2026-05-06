package com.example.qlrp.repository;

import com.example.qlrp.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByShowDateOrderByShowTimeAsc(LocalDate showDate);

    List<Showtime> findByRoom_RoomIdAndShowDate(Integer roomId, LocalDate showDate);

    @Query("SELECT s FROM Showtime s WHERE " +
            "(:movieId IS NULL OR s.movie.movieId = :movieId) AND " +
            "(:roomId IS NULL OR s.room.roomId = :roomId) AND " +
            "(:showDate IS NULL OR s.showDate = :showDate) " +
            "ORDER BY s.showDate DESC, s.showTime DESC")
    List<Showtime> findWithFilters(
            @Param("movieId") Long movieId,
            @Param("roomId") Long roomId,
            @Param("showDate") LocalDate showDate);

    @Query("SELECT DISTINCT s.showDate FROM Showtime s WHERE s.movie.movieId = :movieId AND s.showDate >= :today ORDER BY s.showDate ASC")
    List<LocalDate> findAvailableDatesByMovie(@Param("movieId") Integer movieId, @Param("today") LocalDate today);

    @Query("SELECT s FROM Showtime s WHERE s.movie.movieId = :movieId AND s.showDate = :date ORDER BY s.showTime ASC")
    List<Showtime> findShowtimesByMovieAndDate(@Param("movieId") Integer movieId, @Param("date") LocalDate date);
}
