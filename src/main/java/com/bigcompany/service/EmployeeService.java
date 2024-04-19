package com.bigcompany.service;

import com.bigcompany.annotation.Inject;
import com.bigcompany.dao.EmployeeFileDao;
import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.IncorrectDataException;
import com.bigcompany.model.EmployeeLineInfo;
import com.bigcompany.model.ManagerSalaryInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Core business logic class to work with Employees related flows
 * */
public class EmployeeService {

    @Inject
    private EmployeeFileDao employeeFileDao;

    /**
     * Gets a list of all Managers that earn less than they should, and by how much.
     * @param filePath Path to the file with data
     * @param lowerSalaryLimit percentage from 0 to 100 of average salary of direct subordinates that compares with managers salary
     * */
    public List<ManagerSalaryInfo> getManagersWithLessEarnings(String filePath, int lowerSalaryLimit) {
        List<ManagerSalaryInfo> result = new ArrayList<>();
        List<EmployeeEntity> employees = employeeFileDao.getAllEmployees(filePath);

        for (EmployeeEntity manager : employees) {
            BigDecimal averageSalary = getSubordinatesAverageSalary(manager, employees);
            BigDecimal coefficient = BigDecimal.ONE.add(new BigDecimal(lowerSalaryLimit).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            if (averageSalary.compareTo(BigDecimal.ZERO) != 0 &&
                    manager.salary().divide(averageSalary, 2, RoundingMode.HALF_UP).compareTo(coefficient) < 0) {
                result.add(ManagerSalaryInfo.toManagerSalaryInfo(manager, averageSalary.multiply(coefficient).subtract(manager.salary())));
            }
        }
        return result;
    }

    /**
     * Gets a list of all Managers that earn more than they should, and by how much.
     * @param filePath Path to the file with data
     * @param upperSalaryLimit percentage from 0 to 100 of average salary of direct subordinates that compares with managers salary
     * */
    public List<ManagerSalaryInfo> getManagersWithMoreEarnings(String filePath, int upperSalaryLimit) {
        List<ManagerSalaryInfo> result = new ArrayList<>();
        List<EmployeeEntity> employees = employeeFileDao.getAllEmployees(filePath);

        for (EmployeeEntity manager : employees) {
            BigDecimal averageSalary = getSubordinatesAverageSalary(manager, employees);
            BigDecimal coefficient = BigDecimal.ONE.add(new BigDecimal(upperSalaryLimit).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            if (averageSalary.compareTo(BigDecimal.ZERO) != 0 &&
                    manager.salary().divide(averageSalary, 2, RoundingMode.HALF_UP).compareTo(coefficient) > 0) {
                result.add(ManagerSalaryInfo.toManagerSalaryInfo(manager, manager.salary().subtract(averageSalary.multiply(coefficient))));
            }
        }
        return result;
    }

    /**
     * Gets a list of all Employees that have a reporting line which is too long, and by how much comparing with <code>lineLimit</code>
     * @param filePath Path to the file with data
     * @param lineLimit the maximum allowed reporting line
     * @throws IncorrectDataException when employee has not existing manager id or in case of cyclic hierarchy
     * */
    public List<EmployeeLineInfo> getEmployeesWithLongReportingLines(String filePath, int lineLimit) {
        List<EmployeeLineInfo> result = new ArrayList<>();
        List<EmployeeEntity> employees = employeeFileDao.getAllEmployees(filePath);
        for (EmployeeEntity employee : employees) {
            int reportingLine = getReportingLine(employee, employees);
            if (reportingLine > lineLimit) {
                result.add(EmployeeLineInfo.toEmployeeLineInfo(employee, reportingLine - lineLimit));
            }
        }
        return result;
    }

    /**
     * Calculates reporting line for the employee
     * */
    private int getReportingLine(EmployeeEntity employee, List<EmployeeEntity> employees) {
        int result = 0;
        EmployeeEntity current = employee;
        while (current.managerId() != null) {
            result++;
            String id = current.id();
            current = getEmployeeById(employees, current.managerId());
            if (current == null) {
                throw new IncorrectDataException(format("Exception! User %s has not existing manager id %s", id,
                                getEmployeeById(employees, id).managerId()));
            }
            if (result > 255) {
                throw new IncorrectDataException(format("Exception! Infinite cycle found: managers %s and %s manage themself",
                        current.id(), current.managerId()));
            }
        }
        return result;
    }

    /**
     * Searches the employee by id
     * */
    private EmployeeEntity getEmployeeById(List<EmployeeEntity> employees, String id) {
        return employees.stream().filter(empl -> empl.id().equals(id)).findFirst().orElse(null);
    }

    /**
     * Calculates average salary of Subordinates
     * */
    private BigDecimal getSubordinatesAverageSalary(EmployeeEntity manager, List<EmployeeEntity> employees) {
        List<BigDecimal> employeesSalary = employees.stream()
                .filter(subordinate -> manager.id().equals(subordinate.managerId()))
                .map(EmployeeEntity::salary).toList();
        if (!employeesSalary.isEmpty()) {
            BigDecimal sum = employeesSalary.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(new BigDecimal(employeesSalary.size()), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
