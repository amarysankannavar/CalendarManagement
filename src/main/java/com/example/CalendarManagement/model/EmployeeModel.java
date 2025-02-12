package com.example.CalendarManagement.model;

import javax.persistence.*;



@Entity
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String workEmail;
    private String officeLocation;
    private boolean isActive=true;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean active){
        this.isActive=active;
    }



    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getWorkEmail(){
        return workEmail;
    }

    public void setWorkEmail(String workEmail){
        this.workEmail=workEmail;
    }

    public String getOfficeLocation(){
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation){
        this.officeLocation=officeLocation;
    }


}
