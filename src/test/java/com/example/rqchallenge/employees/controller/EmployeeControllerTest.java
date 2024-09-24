package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setupTestData(){
        // Create test data
       employee = Employee.builder()
                .employee_name("Manoj")
                .employee_salary("6000000")
                .id("1")
                .build();
    }

    @Test
    public void listAllEmployees_whenGetMethod() throws Exception {
        List<Employee> employeeList = List.of(employee);
        // Create a mock response entity
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(employeeList, HttpStatus.OK);

        given(employeeService.getAllEmployees())
                .willReturn(mockResponseEntity);

        // Mock the RestTemplate exchange method to return the mock response
        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasSize(1)))
                .andExpect( jsonPath("$[0].employee_name", is(employee.getEmployee_name())));

    }

    @Test
    public void listEmployeeById_getMethod() throws Exception {
        // Create a mock response entity
        ResponseEntity<Employee> mockResponseEntity = new ResponseEntity<>(employee, HttpStatus.OK);

        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(mockResponseEntity);
        // Mock the RestTemplate exchange method to return the mock response
        mockMvc.perform(get("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("employee_name", is(employee.getEmployee_name())));
    }

    @Test
    public void should_throw_exception_when_employee_doesnt_exist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException(employee.getId())).when(employeeService).getEmployeeById(employee.getId());

        mockMvc.perform(get("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void removeEmployeeById_whenDeleteMethod() throws Exception {
        // Create a mock response entity
        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(employee.getId(), HttpStatus.OK);

        given(employeeService.deleteEmployee(employee.getId()))
                .willReturn(mockResponseEntity);
        mockMvc.perform(delete("/employees/"+employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void should_throw_exception_when_delete_employee_doesnt_exist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException(employee.getId())).when(employeeService).deleteEmployee(employee.getId());

        mockMvc.perform(delete("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchEmployeesByName_whenGetMethod() throws Exception {
        // Create list of employees
        List<Employee> employeeList = List.of(employee);

        // Create a mock response entity
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(employeeList, HttpStatus.OK);

        given(employeeService.getEmployeesByNameSearch("Manoj"))
                .willReturn(mockResponseEntity);

        mockMvc.perform(get("/employees/search/"+employee.getEmployee_name()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasSize(1)))
                .andExpect( jsonPath("$[0].employee_name", is(employee.getEmployee_name())));

    }

    @Test
    public void createEmployee_whenPostMethod() throws Exception {
        // Create a list of employees
        List<Employee> employeeList = List.of(employee);

        Map<String, Employee> employeeMap = employeeList.stream().collect(Collectors.toMap(Employee::getId, item -> item));

        // Create a mock response entity
        ResponseEntity<Employee> mockResponseEntity = new ResponseEntity<>(employee, HttpStatus.OK);

        given(employeeService.createEmployee(employeeMap)).willReturn(mockResponseEntity);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(employeeMap);

        mockMvc.perform(post("/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonResult))
                .andExpect(jsonPath("$.employee_name", is(employee.getEmployee_name())));
    }

    @Test
    public void highestSalaryOfEmployee_whenGetMethod() throws Exception {
        Integer highestSalary = 6000000;

        ResponseEntity<Integer> mockResponseEntity = new ResponseEntity<>(highestSalary, HttpStatus.OK);

        given(employeeService.getHighestSalaryOfEmployees()).willReturn(mockResponseEntity);

        mockMvc.perform(get("/employees/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
