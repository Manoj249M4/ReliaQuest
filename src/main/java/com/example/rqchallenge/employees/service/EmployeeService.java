package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Value("${GET_ALL_EMPLOYEES}")
    private String getAllEmployees;

    @Value("${GET_EMPLOYEES_BY_NAME_SEARCH}")
    private String getEmployeesByNameSearch;

    @Value("${GET_EMPLOYEE_BY_ID}")
    private String getEmployeesById;

    @Value("${DELETE_EMPLOYEE}")
    private String deleteEmployeesById;

    @Value("${CREATE_EMPLOYEE}")
    private String createEmployee;

    public HttpEntity<List<Employee>> httpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return  new HttpEntity<>(headers);
    }
    /**
     * Retrieves a list of all employees by making an HTTP GET request.
     * <p>
     * This method sends a GET request to a specified URL using the `RestTemplate`
     * and returns the list of employees wrapped in a `ResponseEntity`.
     *
     * @return A `ResponseEntity` containing the list of all employees and an HTTP status of OK.
     */
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<List<Employee>> result = null;
        try{
            logger.debug("Fetching employee details");
            result = restTemplate.exchange(getAllEmployees, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Employee>>() {});
        }catch (Exception e){
            logger.error("Error fetching employee details: {}", e.getMessage());
        }
        assert result != null;
        logger.info("Employee details fetched successfully!");
        return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of employees whose names match the given search string.
     * <p>
     * This method sends an HTTP GET request to search for employees by name. The
     * search is performed using the provided search string, which is appended to
     * the request URL.
     *
     * @param searchString The string used to search for employees by name.
     * @return A `ResponseEntity` containing the list of employees that match the
     *         search string and an HTTP status of OK.
     */
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Inside getEmployeesByNameSearch method");
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<List<Employee>> result = null;
        List<Employee> employeeList = new ArrayList<>();
        try{
            logger.debug("Fetching employee details by name");
            result = restTemplate.exchange(getEmployeesByNameSearch, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Employee>>() {});
            employeeList = Objects.requireNonNull(result.getBody()).stream().filter(emp -> emp.getEmployee_name().contains(searchString)).collect(Collectors.toList());

        }catch (Exception e){
            logger.error("Error fetching employee names: {}", e.getMessage());
        }
        logger.info("Employee details by name fetched successfully!: {}", employeeList);
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    /**
     * Retrieves an employee by their unique identifier (ID).
     * <p>
     * This method sends an HTTP GET request to the specified URL with the given employee ID.
     * It uses `RestTemplate` to exchange the request and retrieve the employee details from
     * an external service.
     *
     * @param id The unique identifier of the employee to be retrieved.
     * @return A `ResponseEntity` containing the `Employee` object if found, along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("Inside getEmployeeById method");
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<Employee> result = null;
        try{
            logger.debug("Fetching employee details by id");
            result = restTemplate.exchange(getEmployeesById +"/"+id, HttpMethod.GET, entity, Employee.class);
        }catch(Exception e) {
            logger.error("Error fetching employee by id: {}", e.getMessage());
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        logger.info("Employee details by id fetched successfully:{}", result.getBody());
        return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
    }

    /**
     * Retrieves the highest salary from the list of employees.
     * <p>
     * This method sends an HTTP GET request to fetch all employee details,
     * sorts the employees by their salary in descending order, and returns
     * the highest salary.
     *
     * @return A `ResponseEntity` containing the highest employee salary as an `Integer`,
     *         along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees(){
        logger.info("Inside getHighestSalaryOfEmployees method");
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<List<Employee>> result;
        Integer highestSalary = null;
        try {
            logger.debug("Fetching highest salary of employees");
            result = restTemplate.exchange(getAllEmployees, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Employee>>() {});
            highestSalary = Integer.parseInt(Objects.requireNonNull(result.getBody()).stream().sorted(Comparator.comparing(Employee::getEmployee_salary).reversed()).findFirst().get().getEmployee_salary()) ;
        }catch (Exception e){
            logger.error("Error fetching highest salary: {}", e.getMessage());
        }
        logger.info("Highest salary fetched successfully!: {}", highestSalary);
        return new ResponseEntity<>(highestSalary, HttpStatus.OK);
    }

    /**
     * Retrieves the names of the top ten highest-earning employees.
     * <p>
     * This method sends an HTTP GET request to fetch all employee details,
     * sorts the employees by salary in descending order, and extracts the names
     * of the top ten employees with the highest salaries. If there are fewer than
     * ten employees, it returns the names of all available employees.
     *
     * @return A `ResponseEntity` containing a list of names of the top ten highest-earning
     *         employees (or fewer if less than 10 exist), along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Inside getTopTenHighestEarningEmployeeNames method");
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<List<Employee>> result = null;
        List<String> nameList = new ArrayList<>();
        try{
            logger.debug("Fetching top ten highest earning employees");
            result = restTemplate.exchange(getAllEmployees, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Employee>>() {});
            Objects.requireNonNull(result.getBody()).sort((e1, e2) -> Integer.compare(Integer.parseInt(e2.getEmployee_salary()), Integer.parseInt(e1.getEmployee_salary())));
            nameList = new ArrayList<>();
            for (Employee emp : result.getBody()){
                nameList.add(emp.getEmployee_name());
            }
        }catch (Exception e){
            logger.error("Error fetching highest earning employees: {}", e.getMessage());
        }
        logger.info("Highest earning employees fetched successfully!");
        return new ResponseEntity<>(nameList.size() > 10 ? nameList.subList(0, 10) : nameList, HttpStatus.OK);
    }

    /**
     * Creates a new employee with the provided input data.
     * <p>
     * This method sends an HTTP POST request to create a new employee using the provided
     * employee input, which is encapsulated in a `Map<String, Object>`.
     *
     * @param employeeInput A map containing the input data for the new employee, including
     *                      necessary fields required for creation.
     * @return A `ResponseEntity` containing the created `Employee` object along with an
     *         HTTP status of CREATED (201).
     */
    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Employee> employeeInput) {
        logger.info("Inside createEmployee method");
        ResponseEntity<Employee> result = null;
        try{
            logger.debug("Creating employee");
            result  = restTemplate.postForEntity(createEmployee, employeeInput, Employee.class);
        }catch (Exception e){
            logger.error("Error creating employee : {}", e.getMessage(), e);
        }

        logger.info("Employee created successfully1 : {}", result.getBody());
        return new ResponseEntity<>(result.getBody(), HttpStatus.CREATED);
    }

    /**
     * Deletes an employee by their unique identifier (ID).
     * <p>
     * This method sends an HTTP DELETE request to remove an employee from the system
     * based on the provided employee ID.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return A `ResponseEntity` containing a message indicating the result of the deletion
     *         (e.g., success or failure), along with an HTTP status of OK.
     */
    @Override
    public ResponseEntity<String> deleteEmployee(String id) {
        logger.info("Inside deleteEmployeeById method");
        HttpEntity<List<Employee>> entity = httpEntity();
        ResponseEntity<String> result = null;
        try{
            logger.debug("Deleting employee");
            result = restTemplate.exchange(deleteEmployeesById+"/"+id, HttpMethod.DELETE, entity, String.class);
        }catch (Exception e){
            logger.info("Error deleting employee: {}", e.getMessage());
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        logger.info("Employee deleted successfully! : {}", id);
        return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
    }
}
