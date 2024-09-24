package com.example.rqchallenge.mockserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
