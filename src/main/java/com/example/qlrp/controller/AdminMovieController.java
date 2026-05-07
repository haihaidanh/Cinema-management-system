package com.example.qlrp.controller;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        
        // Nếu trước đó có lỗi, movieData sẽ chứa dữ liệu cũ để điền lại vào form
        if (!model.containsAttribute("movie")) {
            model.addAttribute("movie", new Movie());
        }
        return "admin-movie";
    }

    // 2. POST /admin/movies - Thêm phim mới
    @PostMapping
    public String addMovie(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        try {
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Thêm phim mới thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("movie", movie);
        }
        return "redirect:/admin/movies";
    }

    // 3. POST /admin/movies/update/{id} - Sửa thông tin phim
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable int id, @ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        try {
            movie.setMovieId(id);
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phim thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("movie", movie);
            redirectAttributes.addFlashAttribute("isEdit", true);
        }
        return "redirect:/admin/movies";
    }

    // 4. POST /admin/movies/delete/{id} - Xóa phim
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa phim vì đã có dữ liệu suất chiếu liên quan.");
        }
        return "redirect:/admin/movies";
    }
}
