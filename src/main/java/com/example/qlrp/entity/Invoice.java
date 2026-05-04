package com.example.qlrp.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceId;

    private float totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Reference to the single User entity

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}