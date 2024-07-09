package com.mindex.challenge.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Compensation {

    private Employee employee;
    private BigDecimal salary;
    private LocalDate effectiveDate;

}
