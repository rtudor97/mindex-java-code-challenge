package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportingStructureServiceImplTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private ReportingStructureServiceImpl reportingStructureService;

    private Employee developerI;

    @BeforeEach
    public void setup() {
        developerI = new Employee();
        developerI.setEmployeeId("1234567");
        developerI.setFirstName("John");
        developerI.setLastName("Doe");
        developerI.setDepartment("Engineering");
        developerI.setPosition("Developer");
    }

    @Test
    public void testFetchReportingStructure_NoReports() {
        when(employeeService.read(anyString())).thenReturn(developerI);

        ReportingStructure actual = reportingStructureService.fetchReportingStructure("1234567");

        assertEquals("1234567", actual.getEmployee().getEmployeeId());
        assertEquals(0, actual.getNumberOfReports());
    }

    @Test
    public void testFetchReportingStructure_OneReport() {
        Employee developerV = new Employee();
        developerV.setEmployeeId("7890");
        developerV.setDirectReports(Arrays.asList(developerI));

        when(employeeService.read("1234567")).thenReturn(developerI);
        when(employeeService.read("7890")).thenReturn(developerV);

        ReportingStructure actual = reportingStructureService.fetchReportingStructure("7890");

        assertEquals("7890", actual.getEmployee().getEmployeeId());
        assertEmployeeEquivalence(developerI, actual.getEmployee().getDirectReports().get(0));
        assertEquals(1, actual.getNumberOfReports());
    }

    @Test
    public void testFetchReportingStructure_MultipleReports() {
        Employee developerV = new Employee();
        developerV.setEmployeeId("123");

        Employee developerIII = new Employee();
        developerIII.setEmployeeId("456");

        Employee devManager = new Employee();
        devManager.setEmployeeId("1");
        devManager.setDirectReports(Arrays.asList(developerV, developerIII));

        when(employeeService.read("123")).thenReturn(developerV);
        when(employeeService.read("456")).thenReturn(developerIII);
        when(employeeService.read("1")).thenReturn(devManager);

        ReportingStructure actual = reportingStructureService.fetchReportingStructure("1");

        assertEquals("1", actual.getEmployee().getEmployeeId());
        assertEmployeeEquivalence(developerV, actual.getEmployee().getDirectReports().get(0));
        assertEmployeeEquivalence(developerIII, actual.getEmployee().getDirectReports().get(1));
        assertEquals(2, actual.getNumberOfReports());
    }

    @Test
    public void testFetchReportingStructure_NestedReports() {
        Employee developerV = new Employee();
        developerV.setEmployeeId("7890");
        developerV.setDirectReports(Arrays.asList(developerI));

        Employee devManager = new Employee();
        devManager.setEmployeeId("1");
        devManager.setDirectReports(Arrays.asList(developerV));

        when(employeeService.read("1234567")).thenReturn(developerI);
        when(employeeService.read("7890")).thenReturn(developerV);
        when(employeeService.read("1")).thenReturn(devManager);

        ReportingStructure actual = reportingStructureService.fetchReportingStructure("1");

        assertEquals("1", actual.getEmployee().getEmployeeId());
        assertEmployeeEquivalence(developerV, actual.getEmployee().getDirectReports().get(0));
        assertEmployeeEquivalence(developerI, actual.getEmployee().getDirectReports().get(0).getDirectReports().get(0));
        assertEquals(2, actual.getNumberOfReports());
    }


    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
