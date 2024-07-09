package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
public class ReportingStructureController {

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/reportingStructure/{id}")
    public ResponseEntity<ReportingStructure> read(@PathVariable("id") String employeeId) {
        log.debug("Received reporting structure request for employee with id: {}", employeeId);
        ReportingStructure reportingStructure = reportingStructureService.fetchReportingStructure(employeeId);

        return ResponseEntity.ok(reportingStructure);
    }
}
