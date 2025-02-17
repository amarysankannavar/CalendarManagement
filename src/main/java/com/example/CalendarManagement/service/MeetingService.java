package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.MeetingDTO;
import com.example.CalendarManagement.Exception.MeetingNotFoundException;
import com.example.CalendarManagement.model.MeetingModel;
import com.example.CalendarManagement.model.MeetingRoomModel;
import com.example.CalendarManagement.repository.MeetingRepo;
import com.example.CalendarManagement.repository.MeetingRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private MeetingRoomRepo meetingRoomRepo;

    // Fetch all meetings and return as MeetingDTO list
    public List<MeetingDTO> getMeetings() {
        return meetingRepo.findAll().stream()
                .map(meeting -> new MeetingDTO(meeting.getId(), meeting.getDescription(), meeting.getAgenda(),
                        meeting.getMeetingRoom().getRoomId(), meeting.getDate(), meeting.getStartTime(), meeting.getEndTime(), meeting.isActive()))
                .collect(Collectors.toList());
    }

    // Fetch meeting by ID
    public MeetingDTO getMeetingById(int meetingId) {
        MeetingModel meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException("Meeting not found"));

        return new MeetingDTO(meeting.getId(), meeting.getDescription(), meeting.getAgenda(),
                meeting.getMeetingRoom().getRoomId(), meeting.getDate(), meeting.getStartTime(), meeting.getEndTime(), meeting.isActive());
    }

    // Add new meeting with validation
    public void addMeeting(MeetingDTO meetingDTO) {
        if (meetingDTO.getMeetingId() != 0 && meetingRepo.existsById(meetingDTO.getMeetingId())) {
            throw new IllegalArgumentException("Meeting ID already exists.");
        }

        // Validate description and agenda
        if (meetingDTO.getDescription() == null || meetingDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Meeting description cannot be empty.");
        }

        if (meetingDTO.getAgenda() == null || meetingDTO.getAgenda().trim().isEmpty()) {
            throw new IllegalArgumentException("Meeting agenda cannot be empty.");
        }

        // Fetch the MeetingRoomModel object by roomId
        MeetingRoomModel meetingRoom = meetingRoomRepo.findById(meetingDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room ID not found"));

        // Create and save new Meeting
        MeetingModel meeting = new MeetingModel(meetingDTO.getDescription(), meetingDTO.getAgenda(),
                meetingRoom, meetingDTO.getDate(), meetingDTO.getStartTime(), meetingDTO.getEndTime(),
                meetingDTO.isActive());
        meetingRepo.save(meeting);
    }

    // Deactivate (delete) meeting
    public boolean deleteMeeting(int meetingId) {
        Optional<MeetingModel> meeting = meetingRepo.findById(meetingId);
        if (meeting.isPresent()) {
            meetingRepo.delete(meeting.get());
            return true;  // Meeting deleted successfully
        } else {
            throw new MeetingNotFoundException("Meeting not found");
        }
    }
}
