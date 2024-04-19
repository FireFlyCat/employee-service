package com.bigcompany.dao;

import com.bigcompany.annotation.Inject;
import com.bigcompany.converter.EmployeeParser;
import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.UnexpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Represents data layer
 * Receives data from file by the path and converts it.
 * */
public class EmployeeFileDao {

    Logger logger = Logger.getLogger(EmployeeFileDao.class.getName());

    @Inject
    private EmployeeParser employeeParser;


    /**
     * Get all Employees from the file by the <code>filePath</code> and converts them into EmployeeEntity
     * @implNote Because there are no restrictions by performance, decided to simplify the code and read all lines together.
     * May have huge impact with big data
     * @param filePath file path where the file with data will be read
     * @throws UnexpectedException when file is unreachable
     * */
    public List<EmployeeEntity> getAllEmployees(String filePath) {
        try {
            List<String> strings = Files.readAllLines(new File(filePath).toPath());
            return strings.stream().skip(1).map(employeeParser::parseString).toList();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception while processing file", e);
            throw new UnexpectedException(format("Failed to load file %s", filePath), e);
        }
    }
}
