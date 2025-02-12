package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.Exception.DuplicateEmailException;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;


    public List<EmployeeDTO> getEmployees() {
        return employeeRepo.findAll().stream()
                .map(emp -> new EmployeeDTO(emp.getId(), emp.getName(), emp.getWorkEmail(), emp.getOfficeLocation(), emp.isActive()))
                .collect(Collectors.toList());
    }


    public EmployeeDTO getEmployeeById(int employeeId) {
        EmployeeModel employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getWorkEmail(), employee.getOfficeLocation(), employee.isActive());
    }



    public void addEmployee(EmployeeDTO empDTO) {
        if (empDTO.getEmployeeId() != 0 && employeeRepo.existsById(empDTO.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }

        // Validate name
        if (empDTO.getName() == null || empDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }

        // Validate email format
        if (!empDTO.getWorkEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Check duplicate email
        if (employeeRepo.findByWorkEmail(empDTO.getWorkEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already in use.");
        }

        // Create and save new Employee
        EmployeeModel emp = new EmployeeModel(empDTO.getName(), empDTO.getWorkEmail(), empDTO.getOfficeLocation(), empDTO.isActive());
        employeeRepo.save(emp);
    }


    public void deleteEmployee(int employeeId) {
        EmployeeModel employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        employee.setActive(false);
        employeeRepo.save(employee);
    }
}
