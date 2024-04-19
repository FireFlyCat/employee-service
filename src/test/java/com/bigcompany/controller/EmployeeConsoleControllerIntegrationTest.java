package com.bigcompany.controller;

import com.bigcompany.config.JavaConfig;
import com.bigcompany.service.ConsoleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class EmployeeConsoleControllerIntegrationTest {


    private EmployeeConsoleController controller;

    private ConsoleService consoleService;

    @BeforeEach
    public  void setUp() {
        consoleService = mock();
        JavaConfig.init(Map.of(ConsoleService.class, consoleService));
        controller = JavaConfig.getInstance().getObject(EmployeeConsoleController.class);
    }


    @Test
    void writeEmployeesWithLongReportingLine_oneFound_hp() {
        //given
        String path = getPath();
        //do
        controller.writeEmployeesWithLongReportingLine(path, 2);
        //verify

        verify(consoleService).consoleString(eq("Id,First Name, Last Name, Reporting Line"));
        verify(consoleService).consoleString(eq("305,Brett,Hardleaf,1"));
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    void writeEmployeesWithLongReportingLine_manyFound_hp() {
        //given
        String path = getPath();
        //do
        controller.writeEmployeesWithLongReportingLine(path, 1);
        //verify

        verify(consoleService).consoleString(eq("Id,First Name, Last Name, Reporting Line"));
        verify(consoleService).consoleString(eq("300,Alice,Hasacat,1"));
        verify(consoleService).consoleString(eq("305,Brett,Hardleaf,2"));
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    void writeEmployeesWithLongReportingLine_incorrectParam_IllegalArgumentException_fail() {
        //given
        String path = getPath();
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () ->  controller.writeEmployeesWithLongReportingLine(path, -2));

        //verify

        assertEquals("Reporting Line limit should be positive non zero number",
                incorrectDataException.getMessage());
    }

    @Test
    void writeManagersWithLessEarnings_oneFound_hp() {
        //given
        String path = getPath();
        //do
        controller.writeManagersWithLessEarnings(path, 20);
        //verify

        verify(consoleService).consoleString(eq("Id,First Name, Last Name, Salary difference"));
        verify(consoleService).consoleString(eq("124,Martin,Chekov,15000.00"));
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    void writeManagersWithLessEarnings_incorrectParam_IllegalArgumentException_fail() {
        //given
        String path = getPath();
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () ->  controller.writeManagersWithLessEarnings(path, 111));
        //verify

        assertEquals("Salary Limit should represent percentage value from 0 to 100",incorrectDataException.getMessage());
    }


    @Test
    void writeManagersWithMoreEarnings_oneFound_hp() {
        //given
        String path = getPath();
        //do
        controller.writeManagersWithMoreEarnings(path, 30);
        //verify

        verify(consoleService).consoleString(eq("Id,First Name, Last Name, Salary difference"));
        verify(consoleService).consoleString(eq("300,Alice,Hasacat,5800.00"));
        verifyNoMoreInteractions(consoleService);
    }

    @Test
    void writeManagersWithMoreEarnings_incorrectParam_IllegalArgumentException_fail() {
        //given
        String path = getPath();
        //do
        IllegalArgumentException incorrectDataException = assertThrowsExactly(IllegalArgumentException.class,
                () ->  controller.writeManagersWithMoreEarnings(path, -1));
        //verify

        assertEquals("Salary Limit should represent percentage value from 0 to 100",incorrectDataException.getMessage());
    }

    private String getPath() {
        return requireNonNull(this.getClass().getClassLoader().getResource("employeeFileExampleFull.txt")).getPath();
    }
}