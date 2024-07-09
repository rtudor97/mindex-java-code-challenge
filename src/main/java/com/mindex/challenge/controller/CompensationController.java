package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
public class CompensationController {

    @Autowired
    private CompensationService compensationService;

    @PutMapping("/compensation/{id}")
    public ResponseEntity<Compensation> create(@PathVariable("id") String employeeId,
                                               @RequestBody Compensation compensation) {
        log.debug("Received compensation creation request for employee with id: {}", employeeId);
        try {
            Compensation createdCompensation = compensationService.create(employeeId, compensation);
            log.debug("Successfully saved compensation information for employee: {}", employeeId);
            return new ResponseEntity<>(createdCompensation, HttpStatus.CREATED);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/compensation/{id}")
    public ResponseEntity<Compensation> read(@PathVariable("id") String employeeId) {
        log.debug("Received compensation retrieval request for employee with id: {}", employeeId);
        try {
            Compensation compensation = compensationService.findByEmployeeId(employeeId);
            return ResponseEntity.ok(compensation);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
