package com.example.qlrp.service;

import com.example.qlrp.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Customer currentCustomer = (Customer) session.getAttribute("currentCustomer");

        // 1. Kiểm tra đăng nhập
        if (currentCustomer == null) {
            response.sendError(401, "Please login first!");
            return false;
        }

        return true;
    }
}
