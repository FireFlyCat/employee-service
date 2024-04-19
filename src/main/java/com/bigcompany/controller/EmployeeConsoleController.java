package com.bigcompany.controller;

import com.bigcompany.annotation.Inject;
import com.bigcompany.exception.IncorrectDataException;
import com.bigcompany.exception.UnexpectedException;
import com.bigcompany.model.EmployeeLineInfo;
import com.bigcompany.model.ManagerSalaryInfo;
import com.bigcompany.service.ConsoleService;
import com.bigcompany.service.EmployeeService;

import java.util.List;

import static com.bigcompany.utils.ValidationUtils.*;
import static java.lang.String.format;


/**
 * EmployeeConsoleController is a connection point between business logic and data representation.
 * Processes Employees related logic and writes the result into the console.
 * */
public class EmployeeConsoleController {

    @Inject
    private EmployeeService employeeService;

    @Inject
    private ConsoleService consoleService;


    /**
     * Writes into the console all Managers that earn less than they should, and by how much.
     * @param filePath Path to the file with data
     * @param lowerSalaryLimit percentage from 0 to 100 of average salary of direct subordinates that compares with managers salary
     * @throws IllegalArgumentException when arguments are not correct or file is not exist
     * @throws IncorrectDataException when the parsing failed
     * @throws UnexpectedException when file is unreachable
     * */
    public void writeManagersWithLessEarnings(String filePath, int lowerSalaryLimit) {
        validateFilePath(filePath);
        validateSalaryLimit(lowerSalaryLimit);

        List<ManagerSalaryInfo> managers = employeeService.getManagersWithLessEarnings(filePath, lowerSalaryLimit);

        consoleManagers(managers);
    }

    /**
     * Writes into the console all Managers that earn more than they should, and by how much.
     * @param filePath Path to the file with data
     * @param upperSalaryLimit percentage from 0 to 100 of average salary of direct subordinates that compares with managers salary
     * @throws IllegalArgumentException when arguments are not correct or file is not exist
     * @throws IncorrectDataException when the parsing failed
     * @throws UnexpectedException when file is unreachable
     * */
    public void writeManagersWithMoreEarnings(String filePath, int upperSalaryLimit) {
        validateFilePath(filePath);
        validateSalaryLimit(upperSalaryLimit);

        List<ManagerSalaryInfo> managers = employeeService.getManagersWithMoreEarnings(filePath, upperSalaryLimit);

        consoleManagers(managers);
    }

    /**
     * Writes into the console all Employees that have a reporting line which is too long, and by how much comparing with <code>lineLimit</code>
     * @param filePath Path to the file with data
     * @param lineLimit the maximum allowed reporting line, must be positive
     * @throws IllegalArgumentException when arguments are not correct or file is not exist
     * @throws IncorrectDataException when the parsing failed, employees has cyclic dependencies or manager id does not exist
     * @throws UnexpectedException when file is unreachable
     * */
    public void writeEmployeesWithLongReportingLine(String filePath, int lineLimit) {
        validateFilePath(filePath);
        validateLineLimit(lineLimit);

        List<EmployeeLineInfo> employees = employeeService.getEmployeesWithLongReportingLines(filePath, lineLimit);

        consoleService.consoleString("Id,First Name, Last Name, Reporting Line");
        employees.forEach(m ->
                consoleService.consoleString(format("%s,%s,%s,%s", m.id(), m.firstName(), m.lastName(), m.reportingLine())));
    }

    private void consoleManagers(List<ManagerSalaryInfo> managers) {
        consoleService.consoleString("Id,First Name, Last Name, Salary difference");
        managers.forEach(m ->
                consoleService.consoleString(format("%s,%s,%s,%s",
                        m.id(), m.firstName(), m.lastName(), m.salaryDifference().toPlainString())));
    }

}
