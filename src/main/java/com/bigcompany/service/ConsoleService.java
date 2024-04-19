package com.bigcompany.service;

/**
 * ConsoleService represents isolation of external APIs like System's console.
 * Contains all manipulations with console
 * */
public class ConsoleService {

    /**
     * Writes in the console received line
     * @param line line to write to the console
     * */
    public void consoleString(String line) {
        System.out.println(line);
    }
}
