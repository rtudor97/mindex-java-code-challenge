package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReportingStructureControllerTest {

    @Mock
    private ReportingStructureService reportingStructureService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new ReportingStructureController(reportingStructureService))
                .build();
    }

    @Test
    public void testRead_noReports() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId("1234");
        ReportingStructure reportingStructure = new ReportingStructure(employee, 0);

        when(reportingStructureService.fetchReportingStructure("1234")).thenReturn(reportingStructure);

        mockMvc.perform(get("/reportingStructure/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfReports").value(0))
                .andExpect(jsonPath("$.employee.employeeId").value("1234"));
    }

    @Test
    public void testRead_withReports() throws Exception {
        Employee developer = new Employee();
        developer.setEmployeeId("1234");

        Employee manager = new Employee();
        manager.setEmployeeId("4567");
        manager.setDirectReports(Arrays.asList(developer));
        ReportingStructure reportingStructure = new ReportingStructure(manager, 1);

        when(reportingStructureService.fetchReportingStructure("4567")).thenReturn(reportingStructure);

        mockMvc.perform(get("/reportingStructure/4567"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfReports").value(1))
                .andExpect(jsonPath("$.employee.employeeId").value("4567"))
                .andExpect(jsonPath("$.employee.directReports[0].employeeId").value("1234"));
    }

    @Test
    public void testRead_nestedReports() throws Exception {
        Employee developerI = new Employee();
        developerI.setEmployeeId("1234");

        Employee developerV = new Employee();
        developerV.setEmployeeId("4567");
        developerV.setDirectReports(Arrays.asList(developerI));

        Employee manager = new Employee();
        manager.setEmployeeId("890");
        manager.setDirectReports(Arrays.asList(developerV));
        ReportingStructure reportingStructure = new ReportingStructure(manager, 2);

        when(reportingStructureService.fetchReportingStructure("890")).thenReturn(reportingStructure);

        mockMvc.perform(get("/reportingStructure/890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfReports").value(2))
                .andExpect(jsonPath("$.employee.employeeId").value("890"))
                .andExpect(jsonPath("$.employee.directReports[0].employeeId").value("4567"))
                .andExpect(jsonPath("$.employee.directReports[0].directReports[0].employeeId").value("1234"));
    }


}
