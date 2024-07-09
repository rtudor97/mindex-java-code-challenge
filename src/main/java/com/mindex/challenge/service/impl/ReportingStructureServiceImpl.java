package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {


    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure fetchReportingStructure(String employeeId) {
        Employee employee = employeeService.read(employeeId);
        log.debug("Found employee with id: {}", employee.getEmployeeId());
        fetchDirectReports(employee);
        log.debug("Populated direct reporting structure for employee with id: {}", employee.getEmployeeId());
        int numberOfReports = calculateDirectReports(employee);
        log.debug("Populated direct reports count for employee with id: {}", employee.getEmployeeId());
        return new ReportingStructure(employee, numberOfReports);
    }

    private void fetchDirectReports(Employee employee) {
        if(null == employee || null == employee.getDirectReports()) {
            return;
        }

        List<Employee> directReports = employee.getDirectReports();
        for(int i = 0; i < directReports.size(); i++) {
            String directReportId = directReports.get(i).getEmployeeId();
            Employee directReport = employeeService.read(directReportId);
            fetchDirectReports(directReport);
            directReports.set(i, directReport);
        }

        employee.setDirectReports(directReports);
    }

    private int calculateDirectReports(Employee employee) {
        if(null == employee || null == employee.getDirectReports()) {
            return 0;
        }

        int numberOfReports = 0;
        for(Employee directReport : employee.getDirectReports()) {
            numberOfReports += 1 + calculateDirectReports(directReport);
        }

        return numberOfReports;
    }
}
