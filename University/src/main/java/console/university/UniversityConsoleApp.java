package console.university;

import console.university.exception.RecordNotFoundException;
import console.university.service.UniversityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.Scanner;

@SpringBootApplication
public class UniversityConsoleApp {
    private static final String WHO_IS_HEAD = "Who is head of department ";
    private static final String STATISTIC = "Show ";
    private static final String STATISTIC_ENDING = " statistics";
    private static final String AVERAGE_SALARY = "Show the average salary for the department ";
    private static final String EMPLOYEE_COUNT = "Show count of employee for ";
    private static final String GLOBAL_SEARCH = "Global search by ";

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(UniversityConsoleApp.class, args);
        UniversityService service = context.getBean(UniversityService.class);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome! Write command (or 'exit' to quit):");
        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equals("exit")) {
                System.out.println("Exiting the application.");
                break;
            }
            try {
                if (startsWithCommand(userInput, WHO_IS_HEAD)) {
                    String facultyTitle = userInput.substring(WHO_IS_HEAD.length());
                    System.out.printf("Head of %s department is %s" + System.lineSeparator(),
                            facultyTitle,
                            service.getFacultyHead(facultyTitle));

                } else if (startsWithCommand(userInput, STATISTIC) && userInput.endsWith(STATISTIC_ENDING)) {
                    String facultyTitle =
                            userInput.substring(STATISTIC.length(), userInput.indexOf(STATISTIC_ENDING));
                    System.out.println(service.getFacultyStatistics(facultyTitle));

                } else if (startsWithCommand(userInput, AVERAGE_SALARY)) {
                    String facultyTitle = userInput.substring(AVERAGE_SALARY.length());
                    System.out.printf("Average salary in %s department is %s"
                                    + System.lineSeparator(),
                            facultyTitle,
                            service.getAverageSalary(facultyTitle));

                } else if (startsWithCommand(userInput, EMPLOYEE_COUNT)) {
                    String facultyTitle = userInput.substring(EMPLOYEE_COUNT.length());
                    System.out.println(service.getEmployeeCount(facultyTitle));

                } else if (startsWithCommand(userInput, GLOBAL_SEARCH)) {
                    String searchPattern = userInput.substring(GLOBAL_SEARCH.length());
                    String response = service.searchAll(searchPattern);
                    System.out.println(Objects.isNull(response) ? "No matches found" : response);

                } else {
                    System.out.println("Command not found: " + userInput);
                }
            } catch (RecordNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }

    private static boolean startsWithCommand(String userInput, String command) {
        return userInput.startsWith(command) && userInput.length() >= command.length();
    }
}
