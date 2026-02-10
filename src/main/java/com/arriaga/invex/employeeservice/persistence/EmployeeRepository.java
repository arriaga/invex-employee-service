package com.arriaga.invex.employeeservice.persistence;

import com.arriaga.invex.employeeservice.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
