package com.arriaga.invex.employeeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arriaga.invex.employeeservice.domain.Employee;
import com.arriaga.invex.employeeservice.exception.EmployeeNotFoundException;
import com.arriaga.invex.employeeservice.persistence.EmployeeRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

  @Mock
  private EmployeeRepository repository;

  @Captor
  private ArgumentCaptor<Employee> employeeCaptor;

  private EmployeeServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new EmployeeServiceImpl(repository);
  }

  @Test
  void createDefaultsActiveAndTrimsFields() {
    Employee employee = new Employee();
    employee.setFirstName(" Ana ");
    employee.setLastName(" Ruiz ");
    employee.setSex(" F ");
    employee.setPosition(" Engineer ");

    when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Employee saved = service.create(employee);

    assertThat(saved.getActive()).isTrue();
    assertThat(saved.getFirstName()).isEqualTo("Ana");
    assertThat(saved.getLastName()).isEqualTo("Ruiz");
    assertThat(saved.getSex()).isEqualTo("F");
    assertThat(saved.getPosition()).isEqualTo("Engineer");
  }

  @Test
  void getByIdThrowsWhenMissing() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getById(99L))
        .isInstanceOf(EmployeeNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void updatePartialUpdatesOnlyProvidedFields() {
    Employee existing = new Employee();
    existing.setId(10L);
    existing.setFirstName("Ana");
    existing.setLastName("Ruiz");
    existing.setAge(30);

    Employee updates = new Employee();
    updates.setFirstName(" Maria ");
    updates.setAge(31);
    updates.setBirthDate(LocalDate.of(1993, 1, 1));

    when(repository.findById(10L)).thenReturn(Optional.of(existing));
    when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Employee saved = service.updatePartial(10L, updates);

    assertThat(saved.getFirstName()).isEqualTo("Maria");
    assertThat(saved.getLastName()).isEqualTo("Ruiz");
    assertThat(saved.getAge()).isEqualTo(31);
    assertThat(saved.getBirthDate()).isEqualTo(LocalDate.of(1993, 1, 1));

    verify(repository).save(employeeCaptor.capture());
    assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Maria");
  }
}
