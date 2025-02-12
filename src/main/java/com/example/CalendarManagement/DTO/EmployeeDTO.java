package com.example.CalendarManagement.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmployeeDTO {

    private boolean isActive;
    private int employeeId;

    @NotBlank(message = "Employee name cannot be empty.")
    private String name;

    @NotBlank(message = "Work email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String workEmail;

    @NotBlank(message = "Office location cannot be empty.")
    private String officeLocation;





    public EmployeeDTO(int employeeId, String name, String workEmail, String officeLocation, boolean isActive) {
        this.employeeId = employeeId;
        this.name = name;
        this.workEmail = workEmail;
        this.officeLocation = officeLocation;
        this.isActive = isActive;
    }


    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }


    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean active){
        this.isActive=active;
    }
}
