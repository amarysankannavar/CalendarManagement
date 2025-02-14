package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.DTO.MeetingRoomDTO;
import com.example.CalendarManagement.Exception.DuplicateEmailException;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.model.MeetingRoomModel;
import com.example.CalendarManagement.repository.MeetingRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingRoomService {

    @Autowired
    private MeetingRoomRepo meetingRoomRepo;

    public List<MeetingRoomDTO> getMeetingRooms() {
        return meetingRoomRepo.findAll().stream()
                .map(room -> new MeetingRoomDTO(room.getRoomId(), room.getRoomName(), room.getRoomLocation(),room.getOfficeId(), room.isAvailable()))
                .collect(Collectors.toList());
    }

    public void addMeetingRoom(MeetingRoomDTO roomDTO) {
        if (roomDTO.getRoomId() != 0 && meetingRoomRepo.existsById(roomDTO.getRoomId())) {
            throw new IllegalArgumentException("Room ID already exists.");
        }

        // Validate name
        /*if (empDTO.getName() == null || empDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }

        // Validate email format
        if (!empDTO.getWorkEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Check duplicate email
        if (employeeRepo.findByWorkEmail(empDTO.getWorkEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already in use.");
        }*/

        // Create and save new Employee
        MeetingRoomModel room = new MeetingRoomModel(roomDTO.getRoomName(), roomDTO.getRoomLocation(), roomDTO.getOfficeId(), roomDTO.isAvailable());
        meetingRoomRepo.save(room);
    }
}
