package com.example.CalendarManagement.controller;

import com.example.CalendarManagement.DTO.ApiResponse;
import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.DTO.MeetingRoomDTO;
import com.example.CalendarManagement.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MeetingRoomController {

    @Autowired
    MeetingRoomService meetingRoomService;

    @GetMapping("/meetingRooms")
    public ApiResponse<List<MeetingRoomDTO>> getAllMeetingRooms() {
        List<MeetingRoomDTO> rooms = meetingRoomService.getMeetingRooms();
        return new ApiResponse<>("Meeting rooms fetched successfully", 200, rooms, null);
    }

    @PostMapping("/meetingRooms")
    public ApiResponse<String> addMeetingRoom(@Valid @RequestBody MeetingRoomDTO room) {
        meetingRoomService.addMeetingRoom(room);
        return new ApiResponse<>("Meeting Room added successfully", 201, "Success", null);
    }
}
