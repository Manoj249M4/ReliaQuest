package com.example.rqchallenge.mockserver;

import com.example.rqchallenge.mockserver.dto.Employee;
import com.example.rqchallenge.mockserver.dto.EmployeeListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MockService {
    @Autowired
    MockRepository mockRepository;

   public List<Employee> getAllEmployees(){
      return mockRepository.getAllEmployees();
   }

    public Employee getEmployeeById(String id){
       return mockRepository.getEmployeeById(id);
    }

    public Employee createEmployee(Map<String, Employee> employeeInput){
      return mockRepository.createEmployee(employeeInput);
    }

    public String deleteEmployeeById(String id) throws Exception {
       return mockRepository.deleteEmployeeById(id);
    }

    public EmployeeListResponse getAllEmployeesResearch(){
       return mockRepository.getAllEmployeesResearch();
    }
}
