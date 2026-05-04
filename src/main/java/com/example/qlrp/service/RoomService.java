package com.example.qlrp.service;

import com.example.qlrp.entity.Room;
import com.example.qlrp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }
}
