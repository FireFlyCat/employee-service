package com.bigcompany.model;

import com.bigcompany.entity.EmployeeEntity;

public record EmployeeLineInfo (
        String id,
        String firstName,
        String lastName,
        int reportingLine) {

    public static EmployeeLineInfo toEmployeeLineInfo(EmployeeEntity entity, int reportingLine) {
        return new EmployeeLineInfo(entity.id(), entity.firstName(), entity.lastName(), reportingLine);
    }
}
