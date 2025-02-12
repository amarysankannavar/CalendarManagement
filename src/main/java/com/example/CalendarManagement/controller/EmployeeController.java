package com.example.CalendarManagement.controller;


import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService service;

    @GetMapping("/employees")
    public List<EmployeeModel> getProducts() {
        return service.getEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeModel getEmployeeById(@PathVariable int employeeId) {
        return service.getEmployeeById(employeeId);
    }

    @PostMapping("/employees")
    public void addEmployee(@RequestBody EmployeeModel emp) {
        service.addEmployee(emp);
    }

    @PutMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable int employeeId){
        service.deleteEmployee(employeeId);
    }


}
