package com.example.qlrp.service;

import com.example.qlrp.entity.SeatType;
import com.example.qlrp.repository.SeatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatTypeService {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    // Lấy tất cả loại ghế (phục vụ hiển thị bảng chú thích phụ thu)
    // Sequence: Lớp SeatType gọi phương thức lấy mức phụ thu
    public List<SeatType> getAllSeatTypes() {
        return seatTypeRepository.findAll();
    }
}
