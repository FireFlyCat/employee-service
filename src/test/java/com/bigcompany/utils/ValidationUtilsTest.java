package com.bigcompany.utils;

import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void validateLineLimit_limitIsPositive_hp() {
        //do
        ValidationUtils.validateLineLimit(50);
        //verify
        assertTrue(true);
    }

    @Test
    void validateLineLimit_limitIsNegative_IllegalArgumentException_fail() {
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () -> ValidationUtils.validateLineLimit(-50));
        //verify
        assertEquals("Reporting Line limit should be positive non zero number", incorrectDataException.getMessage());
    }

    @Test
    void validateFilePath_fileExists_hp() {
        //given
        String path = requireNonNull(this.getClass().getClassLoader().getResource("employeeFileExampleFull.txt")).getPath();
        //do
        ValidationUtils.validateFilePath(path);
        //verify
        assertTrue(true);
    }

    @Test
    void validateFilePath_fileNotExists_IllegalArgumentException_fail() {
        //given
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () -> ValidationUtils.validateFilePath("WRONG_PATH"));
        //verify
        assertEquals("File WRONG_PATH does not exist", incorrectDataException.getMessage());
    }


    @Test
    void validateSalaryLimit_limitIsPositiveBetween0And100_hp() {
        //do
        ValidationUtils.validateSalaryLimit(50);
        //verify
        assertTrue(true);
    }

    @Test
    void validateSalaryLimit_limitIsNegative_IllegalArgumentException_fail() {
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () -> ValidationUtils.validateSalaryLimit(-50));
        //verify
        assertEquals("Salary Limit should represent percentage value from 0 to 100", incorrectDataException.getMessage());
    }

    @Test
    void validateSalaryLimit_limitIsHuge_IllegalArgumentException_fail() {
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () -> ValidationUtils.validateSalaryLimit(1000));
        //verify
        assertEquals("Salary Limit should represent percentage value from 0 to 100", incorrectDataException.getMessage());
    }
}