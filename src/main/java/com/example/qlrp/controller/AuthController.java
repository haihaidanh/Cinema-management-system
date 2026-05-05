package com.example.qlrp.controller;

import com.example.qlrp.entity.Customer;
import com.example.qlrp.service.CustomerService;
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
    private CustomerService customerService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Customer customer = customerService.authenticate(email, password);

        log.info("customer:{}", customer);
        if (customer != null) {
            session.setAttribute("currentCustomer", customer);
            return "redirect:/";
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
