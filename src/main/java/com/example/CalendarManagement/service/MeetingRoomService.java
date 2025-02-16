package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.DTO.MeetingRoomDTO;
import com.example.CalendarManagement.Exception.DuplicateEmailException;
import com.example.CalendarManagement.Exception.EmployeeNotFoundException;
import com.example.CalendarManagement.Exception.RoomNotFoundException;
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
        if (roomDTO.getRoomName() == null || roomDTO.getRoomName().trim().isEmpty()) {
            throw new IllegalArgumentException("Meeting room name cannot be empty.");
        }

        if (roomDTO.getRoomLocation() == null || roomDTO.getRoomLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Meeting room location cannot be empty.");
        }


        // Create and save new Employee
        MeetingRoomModel room = new MeetingRoomModel(roomDTO.getRoomName(), roomDTO.getRoomLocation(), roomDTO.getOfficeId());
        meetingRoomRepo.save(room);
    }

    public MeetingRoomDTO getMeetingRoomById(int meetingRoomId) {
        MeetingRoomModel meetingRoom = meetingRoomRepo.findById(meetingRoomId)
                .orElseThrow(() -> new RoomNotFoundException("Meeting Room not found"));

        return new MeetingRoomDTO(meetingRoom.getRoomId(), meetingRoom.getRoomName(), meetingRoom.getRoomLocation(), meetingRoom.getOfficeId(), meetingRoom.isAvailable());
    }

    public void deleteMeetingRoom(int roomId) {

        MeetingRoomModel room = meetingRoomRepo.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Meeting Room not found"));

        meetingRoomRepo.delete(room);
    }

    public void updateMeetingRoomAvailability(int roomId, boolean availability) {
        MeetingRoomModel room = meetingRoomRepo.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Meeting Room not found"));
        if (room.isAvailable() == availability) {
            return;
        }

        room.setAvailable(availability);
        meetingRoomRepo.save(room);
    }

}
