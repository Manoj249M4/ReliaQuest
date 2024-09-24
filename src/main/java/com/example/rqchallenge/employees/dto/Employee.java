package com.example.rqchallenge.employees.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    private String id;
    private String employee_name;
    private String employee_salary;
    private String employee_age;
    private String profile_image;

}
