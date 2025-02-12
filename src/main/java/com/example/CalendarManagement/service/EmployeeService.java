package com.example.CalendarManagement.service;


import com.example.CalendarManagement.Exception.DuplicateEmailException;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.loader.Loader.SELECT;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;


    public List<EmployeeModel> getEmployees(){
        return employeeRepo.findActiveEmployees();
    }

    public EmployeeModel getEmployeeById(int employeeId) {
        return employeeRepo.findById(employeeId).orElse(null);
    }

    public void addEmployee(EmployeeModel emp) {
        if (employeeRepo.existsById(emp.getId())) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }


        if (employeeRepo.findByWorkEmail(emp.getWorkEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already in use.");
        }


        if (!emp.getWorkEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }


        if (emp.getName() == null || emp.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        employeeRepo.save(emp);
    }

    public void deleteEmployee(int employeeId) {
        EmployeeModel employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee != null) {

            employee.setActive(false);
            employeeRepo.save(employee); // Update in the database
        }
    }
}
