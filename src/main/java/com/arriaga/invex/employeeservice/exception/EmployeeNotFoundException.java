package com.arriaga.invex.employeeservice.exception;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(Long id) {
    super("Employee not found: " + id);
  }
}
