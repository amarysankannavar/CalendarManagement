package com.example.CalendarManagement.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleMeetingDTO {
    private List<Integer> employeeIds;
    private String description;
    private String agenda;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private int roomId;

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public String getAgenda(){
        return agenda;
    }

    public void setAgenda(String agenda){
        this.agenda=agenda;
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

    public int getRoomId(){
        return roomId;
    }

    public void setRoomId(int roomId){
        this.roomId=roomId;
    }

}
