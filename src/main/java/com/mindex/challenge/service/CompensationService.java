package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation findByEmployeeId(String employeeId);
    Compensation create(String employeeId, Compensation compensation);
}
