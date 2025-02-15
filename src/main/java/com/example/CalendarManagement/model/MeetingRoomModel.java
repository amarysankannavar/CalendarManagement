package com.example.CalendarManagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MeetingRoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String roomName;
    private String roomLocation;
    private int officeId;
    private boolean available=true;

    public MeetingRoomModel(){

    }

    public MeetingRoomModel(String roomName,String roomLocation, int officeId){
        this.roomName=roomName;
        this.roomLocation=roomLocation;
        this.officeId=officeId;
    }

    public int getRoomId(){
        return roomId;
    }

    public void setRoomId(int roomId){
        this.roomId=roomId;
    }

    public String getRoomName(){
        return roomName;
    }

    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public int getOfficeId(){
        return officeId;
    }

    public void setOfficeId(){
        this.officeId =officeId;
    }
    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available){
        this.available=available;
    }

    public String getRoomLocation(){
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation){
        this.roomLocation=roomLocation;
    }

}
