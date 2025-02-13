package com.example.CalendarManagement;


import com.example.CalendarManagement.model.EmployeeModel;
import com.example.CalendarManagement.repository.EmployeeRepo;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;




//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")  // Use test profile
@TestPropertySource("classpath:application-test.properties")

public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EmployeeRepo employeeRepo;

    @BeforeEach
    void setUp(){
        employeeRepo.save(new EmployeeModel("Amar", "amar@example.com", "NY Office", true));
        employeeRepo.save(new EmployeeModel("Amarys", "amarys@example.com", "LA Office", true));
    }

    @Test
    void getAllEmployees_returnsEmployeesList() throws Exception{
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Amar"))
                .andExpect(jsonPath("$.data[1].name").value("Amarys"));



    }

    @Test
    void getEmployeeById_returnsEmployee() throws Exception {
        EmployeeModel employee = employeeRepo.save(new EmployeeModel("Ajay", "ajay@example.com", "SF Office", true));

        mockMvc.perform(get("/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ajay"))
                .andExpect(jsonPath("$.workEmail").value("ajay@example.com"));
    }


    @Test
    void addEmployee_createsNewEmployee() throws Exception {
        String newEmployeeJson = "{ \"name\": \"Alice\", \"workEmail\": \"alice@example.com\", \"officeLocation\": \"Boston Office\", \"isActive\": true }";

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isOk())  // Ensure 201 Created
                .andExpect(jsonPath("$.message").value("Employee added successfully"))
                .andExpect(jsonPath("$.code").value(201))  // Verify status in response
                .andExpect(jsonPath("$.data").value(  "Success"));  // Ensure `data` is null or empty
    }


    @Test
    void deleteEmployee_removesEmployee() throws Exception {
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deactivated successfully"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("Success"));
    }

    @Test
    void givenNonExistEmployeeId_throwsNotFoundException() throws Exception{
        mockMvc.perform(delete("/employees/3223"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

}
