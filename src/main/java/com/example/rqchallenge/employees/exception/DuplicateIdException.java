package com.example.rqchallenge.employees.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String message){
        super(message);
    }
}
