package com.arriaga.invex.employeeservice.api;

import com.arriaga.invex.employeeservice.api.dto.EmployeeCreateRequest;
import com.arriaga.invex.employeeservice.api.dto.EmployeeResponse;
import com.arriaga.invex.employeeservice.api.dto.EmployeeUpdateRequest;
import com.arriaga.invex.employeeservice.domain.Employee;

public final class EmployeeMapper {

  private EmployeeMapper() {
  }

  public static Employee toEntity(EmployeeCreateRequest request) {
    Employee employee = new Employee();
    employee.setFirstName(request.getFirstName());
    employee.setMiddleName(request.getMiddleName());
    employee.setLastName(request.getLastName());
    employee.setSecondLastName(request.getSecondLastName());
    employee.setAge(request.getAge());
    employee.setSex(request.getSex());
    employee.setBirthDate(request.getBirthDate());
    employee.setPosition(request.getPosition());
    employee.setActive(request.getActive());
    return employee;
  }

  public static void applyUpdates(Employee employee, EmployeeUpdateRequest request) {
    if (request.getFirstName() != null) {
      employee.setFirstName(request.getFirstName());
    }
    if (request.getMiddleName() != null) {
      employee.setMiddleName(request.getMiddleName());
    }
    if (request.getLastName() != null) {
      employee.setLastName(request.getLastName());
    }
    if (request.getSecondLastName() != null) {
      employee.setSecondLastName(request.getSecondLastName());
    }
    if (request.getAge() != null) {
      employee.setAge(request.getAge());
    }
    if (request.getSex() != null) {
      employee.setSex(request.getSex());
    }
    if (request.getBirthDate() != null) {
      employee.setBirthDate(request.getBirthDate());
    }
    if (request.getPosition() != null) {
      employee.setPosition(request.getPosition());
    }
    if (request.getActive() != null) {
      employee.setActive(request.getActive());
    }
  }

  public static EmployeeResponse toResponse(Employee employee) {
    EmployeeResponse response = new EmployeeResponse();
    response.setId(employee.getId());
    response.setFirstName(employee.getFirstName());
    response.setMiddleName(employee.getMiddleName());
    response.setLastName(employee.getLastName());
    response.setSecondLastName(employee.getSecondLastName());
    response.setAge(employee.getAge());
    response.setSex(employee.getSex());
    response.setBirthDate(employee.getBirthDate());
    response.setPosition(employee.getPosition());
    response.setCreatedAt(employee.getCreatedAt());
    response.setActive(employee.getActive());
    return response;
  }
}
