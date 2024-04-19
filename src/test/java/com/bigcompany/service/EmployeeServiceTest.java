package com.bigcompany.service;

import com.bigcompany.config.JavaConfig;
import com.bigcompany.converter.EmployeeParser;
import com.bigcompany.dao.EmployeeFileDao;
import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.IncorrectDataException;
import com.bigcompany.model.EmployeeLineInfo;
import com.bigcompany.model.ManagerSalaryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    EmployeeFileDao employeeFileDao;

    EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        employeeFileDao = mock();

        JavaConfig initialize = JavaConfig.init(Map.of(EmployeeFileDao.class, employeeFileDao));
        employeeService = initialize.getObject(EmployeeService.class);
    }

    @Test
    public void getManagersWithLessEarnings_4EmployeesFoundAverageSalaryTheSameAsManagers_oneManagerReturned_hp() {
        //given
        prepareEmployeesSalaryList("100", "90", "100", "110");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithLessEarnings("path", 20);

        //verify
        assertEquals(1, managersWithLessEarnings.size());

        ManagerSalaryInfo managerSalaryInfo = managersWithLessEarnings.get(0);

        assertEquals("Main", managerSalaryInfo.firstName());
        assertEquals("1", managerSalaryInfo.id());
        assertEquals("Manager", managerSalaryInfo.lastName());
        assertEquals(new BigDecimal("20").setScale(0), managerSalaryInfo.salaryDifference().setScale(0));
    }

    @Test
    public void getManagersWithLessEarnings_4EmployeesFoundAverageSalaryTheSameAsLimit_noManagersReturned_hp() {
        //given
        prepareEmployeesSalaryList("120", "90", "100", "110");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithLessEarnings("path", 20);

        //verify
        assertEquals(0, managersWithLessEarnings.size());
        verify(employeeFileDao).getAllEmployees(eq("path"));
        verifyNoMoreInteractions(employeeFileDao);
    }

    @Test
    public void getManagersWithLessEarnings_4EmployeesFoundAverageSalaryMoreThanManagerSalary_oneManagersReturned_hp() {
        //given
        prepareEmployeesSalaryList("100", "50", "150", "250");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithLessEarnings("path", 20);

        //verify
        assertEquals(1, managersWithLessEarnings.size());

        ManagerSalaryInfo managerSalaryInfo = managersWithLessEarnings.get(0);

        assertEquals("Main", managerSalaryInfo.firstName());
        assertEquals("1", managerSalaryInfo.id());
        assertEquals("Manager", managerSalaryInfo.lastName());
        //average 150 * 1.2 - 100
        assertEquals(new BigDecimal("80").setScale(0), managerSalaryInfo.salaryDifference().setScale(0));
    }

    @Test
    public void getManagersWithMoreEarnings_4EmployeesFoundAverageSalaryTwiceSmallerAsManagers_oneManagerReturned_hp() {
        //given
        prepareEmployeesSalaryList("200", "90", "100", "110");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithMoreEarnings("path", 50);

        //verify
        assertEquals(1, managersWithLessEarnings.size());

        ManagerSalaryInfo managerSalaryInfo = managersWithLessEarnings.get(0);

        assertEquals("Main", managerSalaryInfo.firstName());
        assertEquals("1", managerSalaryInfo.id());
        assertEquals("Manager", managerSalaryInfo.lastName());
        assertEquals(new BigDecimal("50").setScale(0), managerSalaryInfo.salaryDifference().setScale(0));
    }

    @Test
    public void getManagersWithMoreEarnings_4EmployeesFoundAverageSalaryTheSameAsManagers_noManagersReturned_hp() {
        //given
        prepareEmployeesSalaryList("100", "90", "100", "110");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithMoreEarnings("path", 50);

        //verify
        assertEquals(0, managersWithLessEarnings.size());
        verify(employeeFileDao).getAllEmployees(eq("path"));
        verifyNoMoreInteractions(employeeFileDao);
    }

    @Test
    public void getManagersWithMoreEarnings_4EmployeesFoundAverageSalaryTheSameAsLimit_noManagersReturned_hp() {
        //given
        prepareEmployeesSalaryList("150", "90", "100", "110");

        //do
        List<ManagerSalaryInfo> managersWithLessEarnings = employeeService.getManagersWithMoreEarnings("path", 50);

        //verify
        assertEquals(0, managersWithLessEarnings.size());
    }

    @Test
    public void getEmployeesWithLongReportingLines_7EmployeesFoundOneWithLimitReached_oneEmployeeReturned_hp() {
        //given
        prepareEmployeeLineList();

        //do
        List<EmployeeLineInfo> employees = employeeService.getEmployeesWithLongReportingLines("path", 4);

        //verify
        assertEquals(1, employees.size());
        EmployeeLineInfo employeeLineInfo = employees.get(0);

        assertEquals("Employee", employeeLineInfo.firstName());
        assertEquals("6", employeeLineInfo.id());
        assertEquals("File", employeeLineInfo.lastName());
        assertEquals(1, employeeLineInfo.reportingLine());
    }

    @Test
    public void getEmployeesWithLongReportingLines_7EmployeesFoundThreeWithLimitReached_3EmployeesReturned_hp() {
        //given
        prepareEmployeeLineList();

        //do
        List<EmployeeLineInfo> employees = employeeService.getEmployeesWithLongReportingLines("path", 3);

        //verify
        assertEquals(3, employees.size());

        EmployeeLineInfo manager4 = employees.stream().filter(e -> e.id().equals("5")).findFirst().orElseThrow();
        assertEquals(1, manager4.reportingLine());

        EmployeeLineInfo manager44 = employees.stream().filter(e -> e.id().equals("55")).findFirst().orElseThrow();
        assertEquals(1, manager44.reportingLine());

        EmployeeLineInfo manager5 = employees.stream().filter(e -> e.id().equals("6")).findFirst().orElseThrow();
        assertEquals(2, manager5.reportingLine());
    }

    @Test
    public void getEmployeesWithLongReportingLines_7EmployeesFoundNoOneWithLimit_noEmployeesReturned_hp() {
        //given
        prepareEmployeeLineList();

        //do
        List<EmployeeLineInfo> employees = employeeService.getEmployeesWithLongReportingLines("path", 5);

        //verify
        assertEquals(0, employees.size());
        verify(employeeFileDao).getAllEmployees(eq("path"));
        verifyNoMoreInteractions(employeeFileDao);
    }

    @Test
    public void getEmployeesWithLongReportingLines_managerNotExist_IncorrectDataException_fail() {
        //given
        EmployeeEntity manager = new EmployeeEntity("1", "Main", "Manager", BigDecimal.TEN, null);
        EmployeeEntity manager1 = new EmployeeEntity("2", "Employee", "One", BigDecimal.TEN, "123");
        when(employeeFileDao.getAllEmployees(eq("path"))).thenReturn(List.of(manager, manager1));
        //do
        IncorrectDataException incorrectDataException = assertThrowsExactly(IncorrectDataException.class,
                () -> employeeService.getEmployeesWithLongReportingLines("path", 5));

        //verify
        assertEquals("Incorrect Data Exception: Exception! User 2 has not existing manager id 123",
                incorrectDataException.getMessage());
    }

    @Test
    public void getEmploy7eesWithLongReportingLines_infiniteLoopPreventing_IncorrectDataException_fail() {
        //given
        EmployeeEntity manager = new EmployeeEntity("1", "Main", "Manager", BigDecimal.TEN, "2");
        EmployeeEntity manager1 = new EmployeeEntity("2", "Employee", "One", BigDecimal.TEN, "1");
        when(employeeFileDao.getAllEmployees(eq("path"))).thenReturn(List.of(manager, manager1));
        //do
        IncorrectDataException incorrectDataException = assertThrowsExactly(IncorrectDataException.class,
                () -> employeeService.getEmployeesWithLongReportingLines("path", 5));

        //verify
        assertEquals("Incorrect Data Exception: Exception! Infinite cycle found: managers 1 and 2 manage themself",
                incorrectDataException.getMessage());
    }


    private void prepareEmployeesSalaryList(String managerSalary, String employee1Salary,
                                            String employee2Salary, String employee3Salary) {
        EmployeeEntity manager = new EmployeeEntity("1", "Main", "Manager", new BigDecimal(managerSalary), null);
        EmployeeEntity employee1 = new EmployeeEntity("2", "Employee", "One", new BigDecimal(employee1Salary), "1");
        EmployeeEntity employee2 = new EmployeeEntity("3", "Employee", "Too", new BigDecimal(employee2Salary), "1");
        EmployeeEntity employee3 = new EmployeeEntity("4", "Employee", "Tree", new BigDecimal(employee3Salary), "1");
        when(employeeFileDao.getAllEmployees(eq("path"))).thenReturn(List.of(manager, employee2, employee3, employee1));
    }

    private void prepareEmployeeLineList() {
        EmployeeEntity manager = new EmployeeEntity("1", "Main", "Manager", BigDecimal.TEN, null);
        EmployeeEntity manager1 = new EmployeeEntity("2", "Employee", "One", BigDecimal.TEN, "1");
        EmployeeEntity manager2 = new EmployeeEntity("3", "Employee", "Too", BigDecimal.TEN, "2");
        EmployeeEntity manager3 = new EmployeeEntity("4", "Employee", "Tree", BigDecimal.TEN, "3");
        EmployeeEntity manager4 = new EmployeeEntity("5", "Employee", "For", BigDecimal.TEN, "4");
        EmployeeEntity manager44 = new EmployeeEntity("55", "Employee", "For", BigDecimal.TEN, "4");
        EmployeeEntity manager5 = new EmployeeEntity("6", "Employee", "File", BigDecimal.TEN, "5");
        when(employeeFileDao.getAllEmployees(eq("path"))).thenReturn(List.of(manager, manager1, manager2, manager3, manager4, manager44, manager5));
    }
}