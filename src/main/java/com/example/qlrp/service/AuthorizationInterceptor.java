package com.example.qlrp.service;

import com.example.qlrp.contants.UserRole;
import com.example.qlrp.entity.User;
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
        User currentUser = (User) session.getAttribute("currentUser");

        // 1. Kiểm tra đăng nhập
        if (currentUser == null) {
            response.sendError(401, "Please login first!");
            return false;
        }

        String uri = request.getRequestURI();

        // 2. Phân quyền dựa trên đường dẫn (URI)
        if (uri.startsWith("/api/manager") && currentUser.getRole() != UserRole.MANAGER) {
            response.sendError(403, "Access Denied: Managers only!");
            return false;
        }

        if (uri.startsWith("/api/customer") && currentUser.getRole() != UserRole.CUSTOMER) {
            response.sendError(403, "Access Denied: Customers only!");
            return false;
        }

        return true;
    }
}
