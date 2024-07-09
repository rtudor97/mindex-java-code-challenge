package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CompensationControllerTest {

    @Mock
    private CompensationService compensationService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Compensation compensation;

    @BeforeEach
    public void setup () {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new CompensationController(compensationService))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Employee employee = new Employee();
        employee.setEmployeeId("1234");

        compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(new BigDecimal(100000));
        compensation.setEffectiveDate(LocalDate.of(2024, 7, 1));
    }

    @Test
    public void testCreate_success() throws Exception {
        when(compensationService.create(anyString(), any())).thenReturn(compensation);

        mockMvc.perform(put("/compensation/1234")
                .content(objectMapper.writeValueAsString(compensation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee.employeeId").value("1234"))
                .andExpect(jsonPath("$.salary").value(100000));
    }

    @Test
    public void testCreate_failure() throws Exception {
        when(compensationService.create(anyString(), any())).thenThrow(new RuntimeException());
        mockMvc.perform(put("/compensation/1234")
                        .content(objectMapper.writeValueAsString(compensation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRead_success() throws Exception {
        when(compensationService.findByEmployeeId(anyString())).thenReturn(compensation);

        mockMvc.perform(get("/compensation/1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee.employeeId").value("1234"))
                .andExpect(jsonPath("$.salary").value(100000));
    }

    @Test
    public void testRead_failure() throws Exception {
        when(compensationService.findByEmployeeId(anyString())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/compensation/1234"))
                .andExpect(status().isInternalServerError());
    }
}
