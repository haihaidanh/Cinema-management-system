package com.example.qlrp.entity;

import com.example.qlrp.contants.UserRole;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    private String password;

    private String fullName;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('MANAGER', 'CUSTOMER')")
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Invoice> invoices;
}