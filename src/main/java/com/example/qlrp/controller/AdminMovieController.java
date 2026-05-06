package com.example.qlrp.controller;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {
    @Autowired
    private MovieService movieService;

    // 1. GET /admin/movies - Hiển thị danh sách & Tìm kiếm
    @GetMapping
    public String listMovies(@RequestParam(required = false) String keyword, Model model) {
        List<Movie> movies;
        if (keyword != null && !keyword.trim().isEmpty()) {
            movies = movieService.searchByTitle(keyword);
        } else {
            movies = movieService.getAllMovies();
        }
        model.addAttribute("movies", movies);
        model.addAttribute("keyword", keyword);
        model.addAttribute("movie", new Movie()); // Cho Form Add/Edit
        return "admin-movie";
    }

    // 2. POST /admin/movies - Thêm phim mới
    @PostMapping
    public String addMovie(@ModelAttribute Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/admin/movies";
    }

    // 3. POST /admin/movies/update/{id} - Sửa thông tin phim
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable int id, @ModelAttribute Movie movie) {
        movie.setMovieId(id);
        movieService.saveMovie(movie);
        return "redirect:/admin/movies";
    }

    // 4. POST /admin/movies/delete/{id} - Xóa phim
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }
}
