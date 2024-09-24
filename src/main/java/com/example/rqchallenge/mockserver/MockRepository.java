package com.example.rqchallenge.mockserver;

import com.example.rqchallenge.mockserver.dto.Employee;
import com.example.rqchallenge.employees.exception.DuplicateIdException;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.mockserver.dto.EmployeeListResponse;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MockRepository {
    static private final Map<String, Employee> employees = new HashMap<>();
    static {
        Employee employee = new Employee("1", "John", "3000", "25", "https://picsum.photos/id/1/200/300");
        Employee employee2 = new Employee("2", "Jay", "3200", "27", "https://picsum.photos/id/1/200/300");
        employees.put(employee.getId(), employee);
        employees.put(employee2.getId(), employee2);
    }

    public List<Employee> getAllEmployees(){
        return new ArrayList<>(employees.values());
    }

    public <V> EmployeeListResponse getAllEmployeesResearch(){
        EmployeeListResponse employeeList = new EmployeeListResponse();
        ArrayList<Employee> al = new ArrayList<>(employees.values()) ;
        employeeList.setData(al);
        employeeList.setStatus("success");
        return employeeList;
    }

    public Employee getEmployeeById(String id){
        if (employees.get(id) == null){
            throw new ResourceNotFoundException("Employee with id: "+ id+ " does not exist!");
        }
        return employees.get(id);
    }

    public Employee  createEmployee(Map<String, Employee> employeeInput){
        String key = employeeInput.entrySet().iterator().next().getKey();
        Employee value = employeeInput.entrySet().iterator().next().getValue();
        if (employees.containsKey(key)){
            throw new DuplicateIdException("Value "+key+" Already exist");
        }
        if(key.equals(value.getId())){
            Employee emp = Employee.builder()
                    .employee_age(value.getEmployee_age())
                    .employee_salary(value.getEmployee_salary())
                    .employee_name(value.getEmployee_name())
                    .id(value.getId())
                    .profile_image(value.getProfile_image())
                    .build();
            employees.put(key, emp);
            return value;
        }
        return null;
    }

    public String deleteEmployeeById(String id) {
        if (employees.containsKey(id)){
            employees.remove(id);
            return "successfully! deleted Record";
        }

        else {
            throw new ResourceNotFoundException("Resource not found");
        }
    }
}