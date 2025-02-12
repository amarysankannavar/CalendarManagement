package com.example.CalendarManagement.service;

import com.example.CalendarManagement.DTO.EmployeeDTO;
import com.example.CalendarManagement.Exception.DuplicateEmailException;
import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.repository.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployee_givenValidEmployeeDTO_returnsSuccess() {
        EmployeeDTO empDTO = new EmployeeDTO(1, "Amar", "amar@capillary.com", "Bangalore", true);
        EmployeeModel empModel = new EmployeeModel(empDTO.getName(), empDTO.getWorkEmail(), empDTO.getOfficeLocation(), empDTO.isActive());

        when(employeeRepo.existsById(empDTO.getEmployeeId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(empDTO.getWorkEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> employeeService.addEmployee(empDTO));

        verify(employeeRepo, times(1)).save(any(EmployeeModel.class));
    }

    @Test
    void addEmployee_givenDuplicateEmployeeId_throwsException() {
        EmployeeDTO empDTO = new EmployeeDTO(1, "New", "amar@example.com", "Bangalore", true);

        when(employeeRepo.existsById(empDTO.getEmployeeId())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(empDTO));
        assertEquals("Employee ID already exists.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }

    @Test
    void addEmployee_givenInvalidEmail_throwsValidationError() {
        EmployeeDTO empDTO = new EmployeeDTO(1, "Sham", "invalid-email", "Bangalore", true);

        when(employeeRepo.existsById(empDTO.getEmployeeId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(empDTO.getWorkEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(empDTO));
        assertEquals("Invalid email format.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }

    @Test
    void addEmployee_givenDuplicateEmail_throwsException() {
        EmployeeDTO empDTO = new EmployeeDTO(1, "Amar", "amar@capillary.com", "Bangalore", true);
        EmployeeModel existingEmployee = new EmployeeModel(empDTO.getName(), empDTO.getWorkEmail(), empDTO.getOfficeLocation(), empDTO.isActive());

        when(employeeRepo.existsById(empDTO.getEmployeeId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(empDTO.getWorkEmail())).thenReturn(Optional.of(existingEmployee));

        Exception exception = assertThrows(DuplicateEmailException.class, () -> employeeService.addEmployee(empDTO));
        assertEquals("Email already in use.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }

    @Test
    void addEmployee_givenEmptyName_throwsValidationError() {
        EmployeeDTO empDTO = new EmployeeDTO(1, "", "amar@example.com", "Bangalore", true);

        when(employeeRepo.existsById(empDTO.getEmployeeId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(empDTO.getWorkEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(empDTO));
        assertEquals("Employee name cannot be empty.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }
}
