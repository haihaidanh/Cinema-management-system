package com.example.qlrp.service;

import com.example.qlrp.entity.Showtime;
import com.example.qlrp.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public void saveShowtime(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(int id) {
        showtimeRepository.deleteById(id);
    }
}
