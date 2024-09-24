package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IEmployeeService {
    ResponseEntity<List<Employee>> getAllEmployees();

    ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString);

    ResponseEntity<Employee> getEmployeeById(String id);

    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    ResponseEntity<Employee> createEmployee(Map<String, Employee> employeeInput);

    ResponseEntity<String> deleteEmployee(String id);
}
