package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.MeetingDTO;
import com.example.CalendarManagement.DTO.MeetingRequestDTO;
import com.example.CalendarManagement.DTO.ScheduleMeetingDTO;
import com.example.CalendarManagement.Exception.EmployeeNotFoundException;
import com.example.CalendarManagement.Exception.MeetingNotFoundException;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.model.MeetingModel;
import com.example.CalendarManagement.model.MeetingRoomModel;
import com.example.CalendarManagement.repository.EmployeeRepo;
import com.example.CalendarManagement.repository.MeetingRepo;
import com.example.CalendarManagement.repository.MeetingRoomRepo;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CalendarManagement.generated.MeetingManage;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private MeetingRoomRepo meetingRoomRepo;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepo employeeRepo;

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

    public boolean canSchedule(MeetingRequestDTO meetingRequestDTO) {

        List<Integer> empIds = meetingRequestDTO.getEmployeeIds();
        for (int empId : empIds) {
            Optional<EmployeeModel> employee = employeeRepo.findById(empId);
            if(!employee.isPresent()){
                throw new EmployeeNotFoundException("Invalid employee ids");
            }
        }

        TTransport transport = null;
        try {
            transport = new TSocket("localhost", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            MeetingManage.Client client = new MeetingManage.Client(protocol);

            String start = String.valueOf(meetingRequestDTO.getStartTime());
            String end = String.valueOf(meetingRequestDTO.getEndTime());
            String date = String.valueOf(meetingRequestDTO.getDate());
            List<Integer> employeeIds = meetingRequestDTO.getEmployeeIds();


            try {
                boolean schedule = client.canScheduleMeeting(employeeIds, date, start, end);
                return schedule;
            } catch (TException e) {
                throw new RuntimeException(e);
            }


        } catch (TTransportException e) {
            throw new RuntimeException(e);
        } finally {
            if (transport != null && transport.isOpen()) {
                transport.close();  // CLOSE TRANSPORT HERE
            }
        }
    }

    @Transactional  // Ensures the database transaction is committed properly
    public MeetingModel saveMeeting(MeetingModel meeting) {
        return meetingRepo.save(meeting);
    }

    public int scheduleMeeting(ScheduleMeetingDTO scheduleMeetingDTO){


        TTransport transport = null;
        int meetingId=0;
        try{
            transport = new TSocket("localhost",9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            MeetingManage.Client client = new MeetingManage.Client(protocol);

            String description = scheduleMeetingDTO.getDescription();
            String agenda = scheduleMeetingDTO.getAgenda();
            String start = String.valueOf(scheduleMeetingDTO.getStartTime());
            String end = String.valueOf(scheduleMeetingDTO.getEndTime());
            String date = String.valueOf(scheduleMeetingDTO.getDate());
            List<Integer> employeeIds = scheduleMeetingDTO.getEmployeeIds();
            int roomId = scheduleMeetingDTO.getRoomId();

            try{
                 meetingId = client.scheduleMeeting(description,agenda,employeeIds,date,start,end,roomId);
            } catch (TException e) {
                throw new RuntimeException(e);
            }
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        } finally {
            if (transport != null && transport.isOpen()) {
                transport.close();  // CLOSE TRANSPORT HERE
            }
        }



        return meetingId;
    }


}
