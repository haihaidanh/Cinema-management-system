package com.example.qlrp.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    private String title;
    private String genre;
    private int releaseYear;
    private String description;

    private String poster_url;
    private int duration;

    @OneToMany(mappedBy = "movie")
    private List<Showtime> showtimes;
}