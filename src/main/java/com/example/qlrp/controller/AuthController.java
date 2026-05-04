package com.example.qlrp.controller;

import com.example.qlrp.contants.UserRole;
import com.example.qlrp.entity.User;
import com.example.qlrp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.authenticate(username, password);

        log.info("user:{}", user);
        if (user != null) {
            session.setAttribute("currentUser", user);

            // Phân quyền hướng trang sau khi đăng nhập
            if (user.getRole() == UserRole.MANAGER) {
                return "redirect:/admin/movies";
            } else {
                return "redirect:/";
            }
        }

        // Nếu sai thông tin, quay lại login với thông báo lỗi
        return "redirect:/auth/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
