package com.example.qlrp.service;

import com.example.qlrp.entity.Customer;
import com.example.qlrp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Logic kiểm tra đăng nhập bằng email
    public Customer authenticate(String email, String password) {
        return customerRepository.findByEmail(email)
                .filter(c -> c.getPassword().equals(password))
                .orElse(null);
    }

    // Lưu hoặc cập nhật Customer
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
