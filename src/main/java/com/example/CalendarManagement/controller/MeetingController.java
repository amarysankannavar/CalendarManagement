package com.example.CalendarManagement.controller;

import com.example.CalendarManagement.DTO.ApiResponse;
import com.example.CalendarManagement.DTO.MeetingDTO;
import com.example.CalendarManagement.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    // Get all meetings
    @GetMapping
    public ApiResponse<List<MeetingDTO>> getAllMeetings() {
        List<MeetingDTO> meetings = meetingService.getMeetings();
        return new ApiResponse<>("Meetings fetched successfully", 200, meetings, null);
    }

    // Get meeting by ID
    @GetMapping("/{meetingId}")
    public ResponseEntity<ApiResponse<MeetingDTO>> getMeetingById(@PathVariable int meetingId) {
        MeetingDTO meeting = meetingService.getMeetingById(meetingId);
        return ResponseEntity.ok(new ApiResponse<>("Meeting fetched successfully", 200, meeting, null));
    }

    // Add new meeting
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addMeeting(@Valid @RequestBody MeetingDTO meetingDTO) {
        meetingService.addMeeting(meetingDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Meeting added successfully", 201, "Success", null));
    }

    // Deactivate (delete) meeting
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<ApiResponse<String>> deleteMeeting(@PathVariable int meetingId) {
        boolean isDeleted = meetingService.deleteMeeting(meetingId);
        if (isDeleted) {
            return ResponseEntity.ok(new ApiResponse<>("Meeting deactivated successfully", 200, "Success", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Meeting not found", 404, null, null));
    }
}
