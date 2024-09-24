package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.employees.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements IEmployeeController{

    @Autowired
    IEmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    /**
     * Retrieves a list of all employees.
     * <p>
     * This method delegates the task of fetching all employees to the `employeeService`.
     * It returns a `ResponseEntity` containing a list of `Employee` objects.
     *
     * @return A `ResponseEntity` containing a list of all `Employee` objects, along with
     *         an HTTP status.
     *
     * @throws IOException If an input/output error occurs while retrieving the employee data.
     */
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        ResponseEntity<List<Employee>> employeeResponse;
        List<Employee> employeeList;
        employeeResponse = employeeService.getAllEmployees();
        employeeList = employeeResponse.getBody();
        assert employeeList != null;
        if (employeeList.isEmpty()){
            throw new ResourceNotFoundException("No employee Information found");
        }
        logger.info("Fetching all employees: {}", employeeService.getAllEmployees());
        return employeeService.getAllEmployees();
    }

    /**
     * Retrieves a list of employees whose names match the given search string.
     * <p>
     * This method delegates the task of searching for employees by name to the `employeeService`.
     * It takes a search string as input and returns a list of employees whose names match or contain
     * the search string.
     *
     * @param searchString The string to search for in employee names.
     * @return A `ResponseEntity` containing a list of `Employee` objects that match the search string,
     *         along with an HTTP status.
     */
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Fetching employees by name: {}", searchString);
        ResponseEntity<List<Employee>> employeeResponse;
        List<Employee> employeeList;
        employeeResponse = employeeService.getEmployeesByNameSearch(searchString);
        employeeList = employeeResponse.getBody();
        assert employeeList != null;
        if (employeeList.isEmpty()){
            throw new ResourceNotFoundException("No employee found with given name: " +searchString);
        }
        return employeeResponse;
    }

    /**
     * Retrieves an employee by their unique identifier (ID).
     * <p>
     * This method attempts to fetch an employee using the provided ID by delegating the task
     * to the `employeeService`. If no employee is found with the given ID, a `ResourceNotFoundException`
     * is thrown.
     *
     * @param id The unique identifier of the employee to be retrieved.
     * @return A `ResponseEntity` containing the `Employee` object if found, along with an HTTP status of OK.
     * @throws ResourceNotFoundException If no employee is found with the provided ID.
     */
    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("Fetching employee by id: {}", id);
        return employeeService.getEmployeeById(id);
    }

    /**
     * Retrieves the highest salary among all employees.
     * <p>
     * This method delegates the task of fetching the highest employee salary
     * to the `employeeService`. It returns the highest salary as an `Integer`.
     *
     * @return A `ResponseEntity` containing the highest employee salary as an `Integer`,
     *         along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Fetching highest salary among all employees:");
        ResponseEntity<Integer> salaryResponse = employeeService.getHighestSalaryOfEmployees();
        Integer highestSalary = salaryResponse.getBody();
        if (null == highestSalary){
            throw new ResourceNotFoundException("No employee information available");
        }
        return salaryResponse;
    }

    /**
     * Retrieves the names of the top ten highest-earning employees.
     * <p>
     * This method delegates the task of fetching the names of the top ten highest-earning employees
     * to the `employeeService`.
     *
     * @return A `ResponseEntity` containing a list of names of the top ten highest-earning
     *         employees, along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching the names of the top ten highest-earning employees:");
        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();
        List<String> salaryList = response.getBody();
        assert salaryList != null;
        if (salaryList.isEmpty()){
            throw new ResourceNotFoundException("No employee information available");
        }
        return response;
    }

    /**
     * Creates a new employee using the provided input data.
     * <p>
     * This method delegates the task of creating a new employee to the `employeeService`.
     * It accepts a map containing the input data necessary for the creation of the employee
     * and returns the result.
     *
     * @param employeeInput A map containing the input data for the new employee, including
     *                      the required fields for creation.
     * @return A `ResponseEntity` containing the created `Employee` object along with an
     *         appropriate HTTP status.
     */
    @Override
    public ResponseEntity<Employee> createEmployee( Map<String, Employee> employeeInput) throws Exception {
        logger.info("Creates a new employee using the provided input data: {}", employeeInput);
        ResponseEntity<Employee> employeeResponseEntity = employeeService.createEmployee(employeeInput);
        Employee employee = employeeResponseEntity.getBody();
        if (null == employee){
            throw new Exception("An error occurred");
        }
        return employeeResponseEntity;
    }

    /**
     * Deletes an employee by their unique identifier (ID).
     * <p>
     * This method attempts to delete an employee using the provided ID by delegating
     * the task to the `employeeService`. If the deletion does not result in a valid response
     * (i.e., if no employee is found with the given ID), a `ResourceNotFoundException`
     * is thrown.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return A `ResponseEntity` containing a message indicating the result of the deletion,
     *         along with an HTTP status of OK.
     * @throws ResourceNotFoundException If no employee is found with the provided ID.
     */
    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        logger.info("Deleting an employee by their unique identifier: {}", id);
        return employeeService.deleteEmployee(id);
    }
}
