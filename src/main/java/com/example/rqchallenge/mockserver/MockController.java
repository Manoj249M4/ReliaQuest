package com.example.rqchallenge.mockserver;

import com.example.rqchallenge.mockserver.dto.Employee;
import com.example.rqchallenge.mockserver.dto.EmployeeListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MockController {

    @Autowired
    MockService mockService;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return mockService.getAllEmployees();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return mockService.getEmployeeById(id);
    }

    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Map<String, Employee> employeeInput){
        return mockService.createEmployee(employeeInput);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEmployeeById(@PathVariable String id) throws Exception {
        return mockService.deleteEmployeeById(id);
    }

    @GetMapping("/research/employees")
        public EmployeeListResponse getAllEmployeesResearch(){
            return mockService.getAllEmployeesResearch();
    }
}
