package com.bigcompany.converter;

import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.IncorrectDataException;

import java.math.BigDecimal;

public class EmployeeParser {

    /**
     * Parse the string and tries to convert it into the EmployeeEntity
     * @param employeeString string to convert
     * @throws IncorrectDataException when the parsing failed
     * */
    public EmployeeEntity parseString(String employeeString) {
        String[] split = employeeString.split(",");
        if (split.length < 4) {
            throw new IncorrectDataException(
                    String.format("Employee line '%s' has incorrect amount of pamareters", employeeString));
        }

        BigDecimal salary;
        try {
            salary = new BigDecimal(split[3]);
        } catch (NumberFormatException e) {
            throw new IncorrectDataException(
                    String.format("Salary %s of Employee Line '%s' has not a numeric format", split[3], employeeString));
        }
        return new EmployeeEntity(split[0], split[1], split[2], salary, split.length != 5 ? null : split[4]);
    }
}
