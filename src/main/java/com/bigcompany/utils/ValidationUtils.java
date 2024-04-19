package com.bigcompany.utils;

import java.io.File;

import static java.lang.String.format;

/**
 * Validation class with simple fail-fast parameters validation.
 */
public class ValidationUtils {

    /**
     * Checks the parameter is positive
     */
    public static void validateLineLimit(int lineLimit) {
        if (lineLimit <= 0) {
            throw new IllegalArgumentException("Reporting Line limit should be positive non zero number");
        }
    }

    /**
     * Checks the file exists
     */
    public static void validateFilePath(String filePath) {
        if (!new File(filePath).exists()) {
            throw new IllegalArgumentException(format("File %s does not exist", filePath));
        }
    }

    /**
     * Checks the parameter is between 0 and 100
     */
    public static void validateSalaryLimit(int salaryLimit) {
        if (salaryLimit < 0 || salaryLimit > 100) {
            throw new IllegalArgumentException("Salary Limit should represent percentage value from 0 to 100");
        }
    }
}
