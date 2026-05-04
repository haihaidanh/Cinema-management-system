package com.example.qlrp.service;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public Movie getMovieById(int id) {
        return movieRepository.findById(id).orElse(null);
    }
}
