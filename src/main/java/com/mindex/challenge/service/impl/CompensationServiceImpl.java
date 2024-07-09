package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation findByEmployeeId(String employeeId) {
        Employee employee = employeeService.read(employeeId);
        Compensation compensation = compensationRepository.findByEmployee(employee);

        if(null == compensation) {
            throw new RuntimeException("Compensation information not found for this employee");
        }

        return compensation;
    }

    @Override
    public Compensation create(String employeeId, Compensation compensation) {
        Employee employee = employeeService.read(employeeId);

        compensation.setEmployee(employee);

        return compensationRepository.save(compensation);
    }
}
