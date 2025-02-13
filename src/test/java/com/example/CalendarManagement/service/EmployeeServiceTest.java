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

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.tuple;
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

    @Test
    void removeEmployee_givenEmployeeId_setsActiveFalse() {
        // Given
        int employeeId = 1;
        EmployeeModel employee = new EmployeeModel();
        employee.setId(employeeId);
        employee.setActive(true); // Initially active

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepo.save(any(EmployeeModel.class))).thenReturn(employee); // Mock save operation

        employeeService.deleteEmployee(employeeId);

        assertFalse(employee.isActive()); // Ensure the active flag is set to false

        verify(employeeRepo, times(1)).findById(employeeId);
        verify(employeeRepo, times(1)).save(employee);
    }

    @Test
    void removeEmployee_givenNonExistingEmployeeId_throwsNotFoundException(){
        int employeeId=2;


        when(employeeRepo.findById(employeeId)).thenReturn(Optional.empty());



        Exception exception = assertThrows(IllegalArgumentException.class,()-> employeeService.deleteEmployee(employeeId));
        assertEquals("Employee not found",exception.getMessage());
        verify(employeeRepo, times(1)).findById(employeeId);
        verify(employeeRepo, never()).save(any(EmployeeModel.class));
    }


    @Test
    void getEmployees_givenValidEmployeeId_returnsEmployeeInfo() {
        // Given
        int employeeId = 1;
        EmployeeModel employee = new EmployeeModel();
        employee.setId(employeeId);
        employee.setName("John Doe");
        employee.setActive(true);

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        EmployeeDTO result = employeeService.getEmployeeById(employeeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(employeeId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.isActive()).isTrue();

        verify(employeeRepo, times(1)).findById(employeeId);
    }

    @Test
    void getEmployees_whenEmployeesExist_returnEmployeeList() {
        // Given
        EmployeeModel employee1 = new EmployeeModel( "John Doe", "john@example.com", "NY Office", true);
        EmployeeModel employee2 = new EmployeeModel( "Jane Doe", "jane@example.com", "LA Office", true);

        List<EmployeeModel> employeeModels = Arrays.asList(employee1, employee2);

        when(employeeRepo.findAll()).thenReturn(employeeModels);

        // When
        List<EmployeeDTO> result = employeeService.getEmployees();

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting( EmployeeDTO::getName, EmployeeDTO::getWorkEmail, EmployeeDTO::getOfficeLocation, EmployeeDTO::isActive)
                .containsExactlyInAnyOrder(
                        tuple( "John Doe", "john@example.com", "NY Office", true),
                        tuple( "Jane Doe", "jane@example.com", "LA Office", true)
                );

        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    void getEmployee_givenNonExistEmployeeId_throwsNotFoundException(){
        int employeeId=23;

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, ()->employeeService.getEmployeeById(employeeId));

        assertEquals("Employee not found",exception.getMessage());

        verify(employeeRepo, times(1)).findById(employeeId);
    }





}
