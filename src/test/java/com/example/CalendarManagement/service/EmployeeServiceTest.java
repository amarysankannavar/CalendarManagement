package com.example.CalendarManagement.service;

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
    void addEmployee_givenValidEmployeeIdNameEmailOffice_returnsSuccess() {
        EmployeeModel emp = new EmployeeModel();
        emp.setId(1);
        emp.setName("Amar");
        emp.setWorkEmail("amar@capillary.com");
        emp.setOfficeLocation("bnglr");
        emp.setActive(true);

        when(employeeRepo.existsById(emp.getId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(emp.getWorkEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> employeeService.addEmployee(emp));

        verify(employeeRepo, times(1)).save(emp);
    }


    @Test
    void addEmployee_givenDuplicateEmployeeId_throwsException() {
        EmployeeModel emp = new EmployeeModel();

        emp.setName("new");
        emp.setWorkEmail("amar@example.com");

        when(employeeRepo.existsById(emp.getId())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp));
        assertEquals("Employee ID already exists.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }


    @Test
    void addEmployee_givenInvalidEmail_throwsValidationError() {
        EmployeeModel emp = new EmployeeModel();

        emp.setName("Sham");
        emp.setWorkEmail("invalid-email");

        when(employeeRepo.existsById(emp.getId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(emp.getWorkEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp));
        assertEquals("Invalid email format.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }


    @Test
    void addEmployee_givenDuplicateEmail_throwsException() {
        EmployeeModel emp = new EmployeeModel();
        emp.setId(1);
        emp.setName("amar");
        emp.setWorkEmail("amar@capillary.com");

        when(employeeRepo.existsById(emp.getId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(emp.getWorkEmail())).thenReturn(Optional.of(emp));

        Exception exception = assertThrows(DuplicateEmailException.class, () -> employeeService.addEmployee(emp));
        assertEquals("Email already in use.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }


    @Test
    void addEmployee_givenEmptyName_throwsValidationError() {
        EmployeeModel emp = new EmployeeModel();
        emp.setId(1);
        emp.setName("");
        emp.setWorkEmail("amar@example.com");

        when(employeeRepo.existsById(emp.getId())).thenReturn(false);
        when(employeeRepo.findByWorkEmail(emp.getWorkEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp));
        assertEquals("Employee name cannot be empty.", exception.getMessage());

        verify(employeeRepo, never()).save(any());
    }
}
