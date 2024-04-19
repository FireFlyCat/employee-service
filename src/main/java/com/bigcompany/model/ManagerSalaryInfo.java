package com.bigcompany.model;

import com.bigcompany.entity.EmployeeEntity;

import java.math.BigDecimal;

public record ManagerSalaryInfo (
        String id,
        String firstName,
        String lastName,
        BigDecimal salaryDifference) {

    public static ManagerSalaryInfo toManagerSalaryInfo(EmployeeEntity entity, BigDecimal salaryDifference) {
        return new ManagerSalaryInfo(entity.id(), entity.firstName(), entity.lastName(), salaryDifference);
    }
}
