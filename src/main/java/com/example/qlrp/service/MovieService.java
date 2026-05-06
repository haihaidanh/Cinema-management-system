package com.example.qlrp.service;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    // Lấy tất cả phim
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Tìm kiếm phim theo tên (UC: Tìm kiếm phim - Extend của Xem danh sách phim)
    public List<Movie> searchByTitle(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Tìm phim theo ID
    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    public void saveMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phim không được để trống");
        }
        if (movie.getDuration() <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0");
        }
        if (movie.getReleaseYear() < 1895) { // Năm ra đời điện ảnh
            throw new IllegalArgumentException("Năm sản xuất không hợp lệ");
        }
        movieRepository.save(movie);
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }
}
