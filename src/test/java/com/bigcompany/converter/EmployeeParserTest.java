package com.bigcompany.converter;

import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.IncorrectDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class EmployeeParserTest {


    EmployeeParser employeeParser;

    @BeforeEach
    public void setUp() {
        employeeParser = new EmployeeParser();
    }


    @Test
    public void parseString_correctLine_hp() {
        //given
        String testLine = "124,Martin,Chekov,45000,123";

        //do
        EmployeeEntity actual = employeeParser.parseString(testLine);

        //verify
        EmployeeEntity expected = new EmployeeEntity("124", "Martin", "Chekov", new BigDecimal(45000), "123");
        assertEquals(expected, actual);
    }

    @Test
    public void parseString_correctLineCEO_hp() {
        //given
        String testLine = "123,Joe,Doe,60000,";

        //do
        EmployeeEntity actual = employeeParser.parseString(testLine);

        //verify
        EmployeeEntity expected = new EmployeeEntity("123", "Joe", "Doe", new BigDecimal(60000), null);
        assertEquals(expected, actual);
    }

    @Test
    public void parseString_tooShortLine_IncorrectDataException_fail() {
        //given
        String testLine = "123,Joe,Doe,";

        //do
        IncorrectDataException incorrectDataException = assertThrowsExactly(IncorrectDataException.class,
                () -> employeeParser.parseString(testLine));

        //verify
        assertEquals("Incorrect Data Exception: Employee line '123,Joe,Doe,' has incorrect amount of pamareters",
                incorrectDataException.getMessage());
    }

    @Test
    public void parseString_incorrectSalary_IncorrectDataException_fail() {
        //given
        String testLine = "123,Joe,Doe,NotANumber,";

        //do
        IncorrectDataException incorrectDataException = assertThrowsExactly(IncorrectDataException.class,
                () -> employeeParser.parseString(testLine));

        //verify
        assertEquals("Incorrect Data Exception: Salary NotANumber of Employee Line '123,Joe,Doe,NotANumber,' has not a numeric format",
                incorrectDataException.getMessage());
    }
}