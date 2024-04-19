package com.bigcompany.dao;

import com.bigcompany.config.JavaConfig;
import com.bigcompany.controller.EmployeeConsoleController;
import com.bigcompany.converter.EmployeeParser;
import com.bigcompany.entity.EmployeeEntity;
import com.bigcompany.exception.UnexpectedException;
import com.bigcompany.service.ConsoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeFileDaoTest {

    EmployeeFileDao employeeFileDao;


    EmployeeParser parser;

    @BeforeEach
    public void setUp() {
        parser = mock();

        JavaConfig initialize = JavaConfig.init(Map.of(EmployeeParser.class, parser));
        employeeFileDao = initialize.getObject(EmployeeFileDao.class);
    }

    @Test
    public void getAllEmployees_correctFileFull_firstLineIgnored_hp() {
        //given

        when(parser.parseString(anyString()))
                .thenReturn(new EmployeeEntity("1", "First", "Last", BigDecimal.TEN, "2"));
        String path = requireNonNull(this.getClass().getClassLoader().getResource("employeeFileExampleFull.txt")).getPath();

        //do
        List<EmployeeEntity> allEmployees = employeeFileDao.getAllEmployees(path);

        //verify
        assertEquals(5, allEmployees.size());
        verify(parser, times(5)).parseString(anyString());
    }

    @Test
    public void getAllEmployees_fileIsEmpty_hp() {
        //given
        String path = requireNonNull(this.getClass().getClassLoader().getResource("emptyFile.txt")).getPath();

        //do
        List<EmployeeEntity> allEmployees = employeeFileDao.getAllEmployees(path);

        //verify
        assertEquals(0, allEmployees.size());
        verifyNoInteractions(parser);
    }

    @Test
    public void getAllEmployees_wrongPath_fail() {
        //given

        //do
        UnexpectedException exception = assertThrowsExactly(UnexpectedException.class,
                () -> employeeFileDao.getAllEmployees("WRONG_PATH"));

        //verify
        assertEquals("Unexpected Exception: Failed to load file WRONG_PATH",
                exception.getMessage());
        verifyNoInteractions(parser);
    }


}