package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CompensationServiceImplTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private CompensationRepository compensationRepository;

    @InjectMocks
    private CompensationServiceImpl compensationService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee();
        employee.setEmployeeId("1234");
        when(employeeService.read("1234")).thenReturn(employee);
    }

    @Test
    public void testFindByEmployeeId_success() {
        Compensation expected = new Compensation();
        expected.setEmployee(employee);
        expected.setSalary(BigDecimal.valueOf(100000));
        expected.setEffectiveDate(LocalDate.of(2024, 7, 1));

        when(compensationRepository.findByEmployee(any())).thenReturn(expected);

        Compensation actual = compensationService.findByEmployeeId("1234");

        assertCompensationEquivalence(expected, actual);
    }

    @Test
    public void testFindByEmployeeId_failure() {
        when(compensationRepository.findByEmployee(any())).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> compensationService.findByEmployeeId("1234"));
    }

    @Test
    public void testCreate() {
        Compensation compensation = new Compensation();
        compensation.setSalary(BigDecimal.valueOf(100000));
        compensation.setEffectiveDate(LocalDate.of(2024, 7, 1));

        when(compensationRepository.save(any())).thenReturn(compensation);

        Compensation actual = compensationService.create("1234", compensation);

        assertEquals(employee, actual.getEmployee());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
