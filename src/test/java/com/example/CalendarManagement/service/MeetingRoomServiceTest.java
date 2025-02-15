package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.MeetingRoomDTO;
import com.example.CalendarManagement.Exception.RoomNotFoundException;
import com.example.CalendarManagement.model.MeetingRoomModel;
import com.example.CalendarManagement.repository.MeetingRoomRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingRoomServiceTest {

    @Mock
    private MeetingRoomRepo meetingRoomRepo;

    @InjectMocks
    private MeetingRoomService meetingRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMeetingRoom_givenValidMeetingRoomDetails_returnsSuccess() {
        MeetingRoomDTO roomDTO = new MeetingRoomDTO(1, "Conference A", "Floor 2", 101, true);
        MeetingRoomModel roomModel = new MeetingRoomModel(roomDTO.getRoomName(), roomDTO.getRoomLocation(), roomDTO.getOfficeId());

        when(meetingRoomRepo.existsById(roomDTO.getRoomId())).thenReturn(false);

        assertDoesNotThrow(() -> meetingRoomService.addMeetingRoom(roomDTO));

        verify(meetingRoomRepo, times(1)).save(any(MeetingRoomModel.class));
    }

    @Test
    void addMeetingRoom_givenDuplicateRoomId_throwsException() {
        MeetingRoomDTO roomDTO = new MeetingRoomDTO(1, "Conference B", "Floor 3", 102, true);

        when(meetingRoomRepo.existsById(roomDTO.getRoomId())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> meetingRoomService.addMeetingRoom(roomDTO));
        assertEquals("Room ID already exists.", exception.getMessage());

        verify(meetingRoomRepo, never()).save(any());
    }

    @Test
    void addMeetingRoom_givenEmptyRoomName_throwsValidationError() {
        MeetingRoomDTO roomDTO = new MeetingRoomDTO(1, "", "Floor 2", 101, true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> meetingRoomService.addMeetingRoom(roomDTO));
        assertEquals("Meeting room name cannot be empty.", exception.getMessage());

        verify(meetingRoomRepo, never()).save(any());
    }

    @Test
    void addMeetingRoom_givenEmptyRoomLocation_throwsValidationError() {
        MeetingRoomDTO roomDTO = new MeetingRoomDTO(1, "Conference A", "", 101, true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> meetingRoomService.addMeetingRoom(roomDTO));
        assertEquals("Meeting room location cannot be empty.", exception.getMessage());

        verify(meetingRoomRepo, never()).save(any());
    }

    @Test
    void deleteMeetingRoom_givenExistingRoomId_setsAvailableFalse() {
        int roomId = 1;
        MeetingRoomModel room = new MeetingRoomModel("Conference A", "Floor 2", 101);
        room.setRoomId(roomId);
        room.setAvailable(true);

        when(meetingRoomRepo.findById(roomId)).thenReturn(Optional.of(room));
        when(meetingRoomRepo.save(any(MeetingRoomModel.class))).thenReturn(room);

        meetingRoomService.deleteMeetingRoom(roomId);

        assertFalse(room.isAvailable());

        verify(meetingRoomRepo, times(1)).findById(roomId);
        verify(meetingRoomRepo, times(1)).save(room);
    }

    @Test
    void deleteMeetingRoom_givenNonExistingRoomId_throwsNotFoundException() {
        int roomId = 2;
        when(meetingRoomRepo.findById(roomId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RoomNotFoundException.class, () -> meetingRoomService.deleteMeetingRoom(roomId));
        assertEquals("Meeting Room not found", exception.getMessage());

        verify(meetingRoomRepo, times(1)).findById(roomId);
        verify(meetingRoomRepo, never()).save(any());
    }

    @Test
    void getMeetingRooms_whenRoomsExist_returnRoomList() {
        MeetingRoomModel room1 = new MeetingRoomModel("Conference A", "Floor 2", 101);
        MeetingRoomModel room2 = new MeetingRoomModel("Conference B", "Floor 3", 102);
        List<MeetingRoomModel> roomModels = Arrays.asList(room1, room2);

        when(meetingRoomRepo.findAll()).thenReturn(roomModels);

        List<MeetingRoomDTO> result = meetingRoomService.getMeetingRooms();

        assertThat(result)
                .hasSize(2)
                .extracting(MeetingRoomDTO::getRoomName, MeetingRoomDTO::getRoomLocation, MeetingRoomDTO::getOfficeId)
                .containsExactlyInAnyOrder(
                        tuple("Conference A", "Floor 2", 101),
                        tuple("Conference B", "Floor 3", 102)
                );

        verify(meetingRoomRepo, times(1)).findAll();
    }

    @Test
    void getMeetingRoomById_givenExistingRoomId_returnsRoomInfo() {
        int roomId = 1;
        MeetingRoomModel room = new MeetingRoomModel("Conference A", "Floor 2", 101);
        room.setRoomId(roomId);
        room.setAvailable(true);

        when(meetingRoomRepo.findById(roomId)).thenReturn(Optional.of(room));

        MeetingRoomDTO result = meetingRoomService.getMeetingRoomById(roomId);

        assertThat(result).isNotNull();
        assertThat(result.getRoomId()).isEqualTo(roomId);
        assertThat(result.getRoomName()).isEqualTo("Conference A");
        assertThat(result.isAvailable()).isTrue();

        verify(meetingRoomRepo, times(1)).findById(roomId);
    }

    @Test
    void getMeetingRoomById_givenNonExistingRoomId_throwsNotFoundException() {
        int roomId = 3;
        when(meetingRoomRepo.findById(roomId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> meetingRoomService.getMeetingRoomById(roomId));
        assertEquals("Employee not found", exception.getMessage());

        verify(meetingRoomRepo, times(1)).findById(roomId);
    }
}
