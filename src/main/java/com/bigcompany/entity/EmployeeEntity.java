package com.bigcompany.entity;

import java.math.BigDecimal;

public record EmployeeEntity(
        String id,
        String firstName,
        String lastName,
        BigDecimal salary,
        String managerId
) {
}
