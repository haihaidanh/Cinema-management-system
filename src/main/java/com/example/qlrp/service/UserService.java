package com.example.qlrp.service;

import com.example.qlrp.entity.User;
import com.example.qlrp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Logic kiểm tra đăng nhập
    public User authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    // Lưu hoặc cập nhật User
    public User save(User user) {
        return userRepository.save(user);
    }
}