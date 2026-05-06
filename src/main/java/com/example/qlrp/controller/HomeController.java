package com.example.qlrp.controller;

import com.example.qlrp.entity.Movie;
import com.example.qlrp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    /**
     * Trang chủ — UC: Xem danh sách phim + Tìm kiếm phim (Extend)
     * Hiển thị tất cả phim đang chiếu, hỗ trợ tìm kiếm theo tên.
     */
    @GetMapping("/")
    public String showHomePage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Movie> movies;
        if (keyword != null && !keyword.isEmpty()) {
            movies = movieService.searchByTitle(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            movies = movieService.getAllMovies();
        }
        model.addAttribute("movies", movies);
        return "home";
    }
}
