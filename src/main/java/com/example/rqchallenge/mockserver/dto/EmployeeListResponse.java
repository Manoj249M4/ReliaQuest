package com.example.rqchallenge.mockserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeListResponse {
    private String status;
    private ArrayList<Employee> data;
}
