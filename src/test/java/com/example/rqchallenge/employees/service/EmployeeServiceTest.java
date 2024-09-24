package com.example.rqchallenge.employees.service;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

import com.example.rqchallenge.employees.dto.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest(classes = EmployeeService.class)
public class EmployeeServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    EmployeeService employeeService;

    private Employee emp;

    private static final  String getAllEmployees = "http://localhost:8080/api/v1/employees";

    private final String getEmployeesByNameSearch = "http://localhost:8080/api/v1/employees";

    private static final String getEmployeesById = "http://localhost:8080/api/v1/employee";

    private static final String deleteEmployeesById = "http://localhost:8080/api/v1/delete";

    private static final String createEmployeeUrl ="http://localhost:8080/api/v1/create";
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

         emp = Employee.builder()
                 .id("1")
                .employee_name("Manoj")
                .employee_salary("600000")
                .employee_age("27")
                .build();
    }

    @Test
    void testGetAllEmployees(){
        List<Employee> mockEmpolyeeList = new ArrayList<>();
        Employee emp = Employee.builder()
                .employee_name("Manoj")
                .employee_salary("600000")
                .employee_age("27")
                .build();
        mockEmpolyeeList.add(emp);
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(mockEmpolyeeList, OK);

        // Mock the RestTemplate exchange method
        when(restTemplate.exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponseEntity);

        // Call the method under test
        ResponseEntity<List<Employee>> response = employeeService.getAllEmployees();

        // Verify the response and the logger output
        assertEquals(OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Manoj", response.getBody().get(0).getEmployee_name());

        // Verify that the RestTemplate exchange method was called once
        verify(restTemplate, times(1)).exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetEmployeesByNameSearch_Success() {
        // Create mock employee list
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(emp);

        // Create a mock response entity
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(mockEmployees, HttpStatus.OK);

        // Mock the RestTemplate exchange method to return the mock response
        when(restTemplate.exchange(
                eq(getEmployeesByNameSearch),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(mockResponseEntity);

        // Define the search string
        String searchString = "Manoj";

        // Call the method under test
        ResponseEntity<List<Employee>> response = employeeService.getEmployeesByNameSearch(searchString);

        // Verify the response
        List<Employee> filteredEmployees = response.getBody();
        assertNotNull(filteredEmployees);
        assertEquals(1, filteredEmployees.size());
        assertEquals("Manoj", filteredEmployees.get(0).getEmployee_name());

        // Verify that the RestTemplate exchange method was called once
        verify(restTemplate, times(1)).exchange(
                eq(getEmployeesByNameSearch),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetEmployeesByNameSearch_Exception() {
        // Simulate an exception in RestTemplate exchange method
        when(restTemplate.exchange(
                eq(getEmployeesByNameSearch),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new RuntimeException("API error"));

        // Define the search string
        String searchString = "Manoj";

        // Call the method under test and ensure that no exception is thrown to the outside
        ResponseEntity<List<Employee>> response = employeeService.getEmployeesByNameSearch(searchString);

        // Verify that an empty list is returned when an exception occurs
        List<Employee> filteredEmployees = response.getBody();
        assertNotNull(filteredEmployees);
        assertEquals(0, filteredEmployees.size());


        // Verify that the RestTemplate exchange method was called once
        verify(restTemplate, times(1)).exchange(
                eq(getEmployeesByNameSearch),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetEmployeeById_Success() {

        // Mock the response entity
        ResponseEntity<Employee> mockResponseEntity = new ResponseEntity<>(emp, HttpStatus.OK);

        // Mock the RestTemplate exchange method
        when(restTemplate.exchange(
                eq(getEmployeesById + "/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Employee.class))
        ).thenReturn(mockResponseEntity);

        // Call the method under test
        ResponseEntity<Employee> response = employeeService.getEmployeeById("1");

        // Verify the response
        assertNotNull(response.getBody());
        assertEquals("Manoj", response.getBody().getEmployee_name());
        assertEquals("600000", response.getBody().getEmployee_salary());

        // Verify interaction with RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(getEmployeesById + "/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Employee.class)
        );
    }


    @Test
    void testGetHighestSalaryOfEmployees_Success() {
        List<Employee> mockEmployeeList = Collections.singletonList(emp);

        // Mock the response entity
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(mockEmployeeList, HttpStatus.OK);

        // Mock the RestTemplate exchange method
        when(restTemplate.exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(mockResponseEntity);

        // Call the method under test
        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();

        // Verify the response
        assertNotNull(response.getBody());

        verify(restTemplate, times(1)).exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_Success() {
        // Create mock employees with different salaries
        Employee emp1 = Employee.builder()
                .employee_name("Manoj")
                .employee_salary("600000").build();
        Employee emp2 = Employee.builder()
                .employee_name("Jane")
                .employee_salary("60000").build();
        Employee emp3 = Employee.builder()
                .employee_name("Mark")
                .employee_salary("190000").build();
        Employee emp4 = Employee.builder()
                .employee_name("Sara")
                .employee_salary("180000").build();
        Employee emp5 = Employee.builder()
                .employee_name("Lucas")
                .employee_salary("170000").build();
        Employee emp6 = Employee.builder()
                .employee_name("Adam")
                .employee_salary("160000").build();
        Employee emp7 = Employee.builder()
                .employee_name("Gary")
                .employee_salary("150000").build();
        Employee emp8 = Employee.builder()
                .employee_name("Roman")
                .employee_salary("140000").build();
        Employee emp9 = Employee.builder()
                .employee_name("Nancy")
                .employee_salary("130000").build();
        Employee emp10 = Employee.builder()
                .employee_name("Anitta")
                .employee_salary("120000").build();
        Employee emp11 = Employee.builder()
                .employee_name("Karen")
                .employee_salary("10000").build();

        // Mock employee list
        List<Employee> mockEmployeeList = Arrays.asList(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10, emp11);

        // Mock the response entity
        ResponseEntity<List<Employee>> mockResponseEntity = new ResponseEntity<>(mockEmployeeList, HttpStatus.OK);

        // Mock RestTemplate call
        when(restTemplate.exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponseEntity);

        // Call the method under test
        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();

        // Verify the response contains top 10 employee names sorted by salary
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().size());
        assertEquals("Manoj", response.getBody().get(0)); // Highest earning employee
        assertEquals("Mark", response.getBody().get(1)); // Second highest
        assertEquals("Sara", response.getBody().get(2)); // Third highest
        // Continue for the rest as needed...

        // Verify interaction with RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_Exception() {
        // Simulate an exception in RestTemplate exchange method
        when(restTemplate.exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("API error"));

        // Call the method under test
        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();

        // Verify that the response is an empty list due to the exception
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());


        // Verify interaction with RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(getAllEmployees),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testDeleteEmployee_Success() {
        // Mock response from the DELETE request
        String employeeId = "1";
        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);

        // Mock RestTemplate DELETE call
        when(restTemplate.exchange(
                eq(deleteEmployeesById + "/" + employeeId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // Call the method under test
        ResponseEntity<String> response = employeeService.deleteEmployee(employeeId);

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());

        // Verify interaction with RestTemplate
        verify(restTemplate, times(1)).exchange(
                eq(deleteEmployeesById + "/" + employeeId),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testCreateEmployee_Success() {
        // Prepare mock input data
        Map<String, Employee> employeeInput = new HashMap<>();
        employeeInput.put("1", emp);

        // Mock the behavior of restTemplate
        ResponseEntity<Employee> mockResponse = new ResponseEntity<>(emp, HttpStatus.CREATED);
        when(restTemplate.postForEntity(eq(createEmployeeUrl), eq(employeeInput), eq(Employee.class)))
                .thenReturn(mockResponse);

        // Call the method under test
        ResponseEntity<Employee> response = employeeService.createEmployee(employeeInput);

        // Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(emp);

        // Verify the interactions
        verify(restTemplate, times(1)).postForEntity(createEmployeeUrl, employeeInput, Employee.class);
    }

}
