package com.bigcompany;

import com.bigcompany.config.JavaConfig;
import com.bigcompany.controller.EmployeeConsoleController;

import static java.util.Objects.requireNonNull;

public class Main {
    /**
     * The starting point of the application.
     * Initializes config and runs methods in controller with default parameters that described in the task.
     * to change file put <code>-DfilePath</code> variable or put first inline console argument
     * By default filePath is <code>employeeFileFull.txt</code>
     */
    public static void main(String[] args) {
        JavaConfig config = JavaConfig.getInstance();
        EmployeeConsoleController controller = config.getObject(EmployeeConsoleController.class);

        String path = getPath(args);

        System.out.println("\nwhich managers earn less than they should, and by how much");
        controller.writeManagersWithLessEarnings(path, 20);

        System.out.println("\nwhich managers earn more than they should, and by how much");
        controller.writeManagersWithMoreEarnings(path, 20);

        System.out.println("\nwhich employees have a reporting line which is too long, and by how much");
        controller.writeEmployeesWithLongReportingLine(path, 4);
    }

    private static String getPath(String[] args) {
        if (System.getProperty("filePath") != null) {
            return System.getProperty("filePath");
        }
        if (args.length != 0) {
            return args[0];
        }
        return requireNonNull(Main.class.getClassLoader().getResource("employeeFileFull.txt")).getPath();
    }
}