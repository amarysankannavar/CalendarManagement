package com.example.CalendarManagement.controller;


import com.example.CalendarManagement.DTO.ApiResponse;
import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService service;

    @GetMapping("/employees")
    public ApiResponse<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = service.getEmployees();
        return new ApiResponse<>("Employees fetched successfully", 200, employees, null);
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable int employeeId) {
        return service.getEmployeeById(employeeId);
    }

    @PostMapping("/employees")
    public ApiResponse<String> addEmployee(@Valid @RequestBody EmployeeDTO emp) {
        service.addEmployee(emp);
        return new ApiResponse<>("Employee added successfully", 201, "Success", null);
    }

    @PutMapping("/employees/{employeeId}")
    public ApiResponse<String> deleteEmployee(@PathVariable int employeeId) {
        service.deleteEmployee(employeeId);
        return new ApiResponse<>("Employee deactivated successfully", 200, "Success", null);
    }


}
