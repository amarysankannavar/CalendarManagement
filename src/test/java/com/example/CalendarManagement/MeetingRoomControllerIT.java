package com.example.CalendarManagement;

import com.example.CalendarManagement.model.MeetingRoomModel;
import com.example.CalendarManagement.repository.MeetingRoomRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class MeetingRoomControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MeetingRoomRepo meetingRoomRepo;

    @BeforeEach
    void setUp() {
        meetingRoomRepo.save(new MeetingRoomModel("Conference Room A", "NY Office", 101));
        meetingRoomRepo.save(new MeetingRoomModel("Conference Room B", "LA Office", 102));
    }

    @Test
    void getAllMeetingRooms_returnsRoomsList() throws Exception {
        mockMvc.perform(get("/meetingRooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].roomName").value("Conference Room A"))
                .andExpect(jsonPath("$.data[1].roomName").value("Conference Room B"));
    }

    @Test
    void getMeetingRoomById_givenRoomId_returnsRoom() throws Exception {
        MeetingRoomModel room = meetingRoomRepo.save(new MeetingRoomModel("Board Room", "SF Office", 103));

        mockMvc.perform(get("/meetingRooms/" + room.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value("Board Room"))
                .andExpect(jsonPath("$.roomLocation").value("SF Office"))
                .andExpect(jsonPath("$.officeId").value(103));
    }

    @Test
    void addMeetingRoom_givenRoomDetails_createsNewRoom() throws Exception {
        String newRoomJson = "{ \"roomName\": \"Training Room\", \"roomLocation\": \"Boston Office\", \"officeId\": 104 }";

        mockMvc.perform(post("/meetingRooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRoomJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Meeting Room added successfully"))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value("Success"));
    }

    @Test
    void deleteMeetingRoom_givenRoomId_removesRoom() throws Exception {
        MeetingRoomModel room = meetingRoomRepo.save(new MeetingRoomModel("Focus Room", "Chicago Office", 105));

        mockMvc.perform(delete("/meetingRooms/" + room.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Meeting Room deactivated successfully"))
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void deleteMeetingRoom_givenNonExistRoomId_throwsNotFoundException() throws Exception {
        mockMvc.perform(delete("/meetingRooms/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Meeting Room not found"));
    }

    @Test
    void updateMeetingRoomAvailability_givenRoomId_updatesAvailability() throws Exception {
        MeetingRoomModel room = meetingRoomRepo.save(new MeetingRoomModel("Quiet Room", "Berlin Office", 106));

        mockMvc.perform(put("/meetingRooms/" + room.getRoomId() + "/availability")
                        .param("availability", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Meeting Room availability updated successfully"))
                .andExpect(jsonPath("$.code").value(404));
    }
}
