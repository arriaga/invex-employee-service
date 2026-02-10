package com.arriaga.invex.employeeservice.service;

import com.arriaga.invex.employeeservice.domain.Employee;
import com.arriaga.invex.employeeservice.persistence.EmployeeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository repository;

  public EmployeeServiceImpl(EmployeeRepository repository) {
    this.repository = repository;
  }

  @Override
  public Employee create(Employee employee) {
    Employee normalized = normalizeForCreate(employee);
    return repository.save(normalized);
  }

  @Override
  public List<Employee> createAll(List<Employee> employees) {
    for (Employee employee : employees) {
      normalizeForCreate(employee);
    }
    return repository.saveAll(employees);
  }

  @Override
  public List<Employee> findAll() {
    return repository.findAll();
  }

  @Override
  public Employee getById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Employee not found: " + id));
  }

  @Override
  public Employee updatePartial(Long id, Employee updates) {
    Employee existing = getById(id);
    if (updates == null) {
      return existing;
    }
    applyUpdates(existing, updates);
    normalizeNames(existing);
    return repository.save(existing);
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  private Employee normalizeForCreate(Employee employee) {
    if (employee.getActive() == null) {
      employee.setActive(Boolean.TRUE);
    }
    normalizeNames(employee);
    return employee;
  }

  private void normalizeNames(Employee employee) {
    employee.setFirstName(trim(employee.getFirstName()));
    employee.setMiddleName(trim(employee.getMiddleName()));
    employee.setLastName(trim(employee.getLastName()));
    employee.setSecondLastName(trim(employee.getSecondLastName()));
    employee.setSex(trim(employee.getSex()));
    employee.setPosition(trim(employee.getPosition()));
  }

  private void applyUpdates(Employee target, Employee updates) {
    if (updates.getFirstName() != null) {
      target.setFirstName(updates.getFirstName());
    }
    if (updates.getMiddleName() != null) {
      target.setMiddleName(updates.getMiddleName());
    }
    if (updates.getLastName() != null) {
      target.setLastName(updates.getLastName());
    }
    if (updates.getSecondLastName() != null) {
      target.setSecondLastName(updates.getSecondLastName());
    }
    if (updates.getAge() != null) {
      target.setAge(updates.getAge());
    }
    if (updates.getSex() != null) {
      target.setSex(updates.getSex());
    }
    if (updates.getBirthDate() != null) {
      target.setBirthDate(updates.getBirthDate());
    }
    if (updates.getPosition() != null) {
      target.setPosition(updates.getPosition());
    }
    if (updates.getActive() != null) {
      target.setActive(updates.getActive());
    }
  }

  private String trim(String value) {
    if (value == null) {
      return null;
    }
    return value.trim();
  }
}
