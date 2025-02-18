package com.example.CalendarManagement.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MeetingRequestDTO {
    private List<Integer> employeeIds;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    // Getters and Setters
    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
