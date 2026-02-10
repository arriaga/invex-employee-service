package com.arriaga.invex.employeeservice.service;

import com.arriaga.invex.employeeservice.domain.Employee;
import java.util.List;

public interface EmployeeService {

  Employee create(Employee employee);

  List<Employee> createAll(List<Employee> employees);

  List<Employee> findAll();

  Employee getById(Long id);

  Employee updatePartial(Long id, Employee updates);

  void deleteById(Long id);
}
