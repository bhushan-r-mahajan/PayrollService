import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PayrollServiceTest {
    List <EmployeeData> employeeData = new ArrayList<>();

    @Test
    void giveDataFromADatabase_WhenRetrieved_ShouldMatchTheCount() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        System.out.println("The Number of Entries in DataBase are = " + employeeData.size());
        Assertions.assertEquals(3, employeeData.size());
    }

    @Test
    void givenNewSalary_WhenUpdated_ShouldPassTestAndBeInSync() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        int result = PayrollService.getInstance().updateDataInDB("shashank", 5000000);
        System.out.println("DataBase and List are in Sync ---->\n" + employeeData.get(0));
        System.out.println(PayrollService.getInstance().getEmployeePayrollData("Shashank"));
        Assertions.assertEquals(1, result);
    }

    @Test
    void givenDate_WhenGiven_ShouldRetrieveEmployeeData() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        employeeData = PayrollService.getInstance().readDataAccordingToDate(startDate, endDate);
        Assertions.assertEquals(2, employeeData.size());
    }

    @Test
    void givenPayrollData_WhenAverageSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> salaryOperations = PayrollService.getInstance().readAverageSalary();
        System.out.println("<-- Average Salary of Male And Female -->\n" + PayrollService.getInstance().readAverageSalary());
        Assertions.assertTrue(salaryOperations.get("m").equals(3500000.0) && salaryOperations.get("f").equals(3000000.0));
    }

    @Test
    void givenPayrollData_WhenSumOfSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> salaryOperations = PayrollService.getInstance().readSumOfSalary();
        System.out.println("<-- Sum Of Salary of Male And Female -->\n" + PayrollService.getInstance().readSumOfSalary());
        Assertions.assertTrue(salaryOperations.get("m").equals(7000000.0) && salaryOperations.get("f").equals(3000000.0));
    }

    @Test
    void givenPayrollData_WhenMinSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> salaryOperations = PayrollService.getInstance().readMinSalary();
        System.out.println("<-- Minimum Salary of Male And Female -->\n" + PayrollService.getInstance().readMinSalary());
        Assertions.assertTrue(salaryOperations.get("m").equals(2000000.0) && salaryOperations.get("f").equals(3000000.0));
    }

    @Test
    void givenPayrollData_WhenMaxSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> salaryOperations = PayrollService.getInstance().readMaxSalary();
        System.out.println("<-- Maximum Salary of Male And Female -->\n" + PayrollService.getInstance().readMaxSalary());
        Assertions.assertTrue(salaryOperations.get("m").equals(5000000.0) && salaryOperations.get("f").equals(3000000.0));
    }

    @Test
    void givenPayrollData_WhenCountOfSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> salaryOperations = PayrollService.getInstance().readCountOfSalary();
        System.out.println("<-- Count Of Salary of Male And Female -->\n" + PayrollService.getInstance().readCountOfSalary());
        Assertions.assertTrue(salaryOperations.get("m").equals(2.0) && salaryOperations.get("f").equals(1.0));
    }

    @Test
    void givenAnEmployee_WhenAddedToDB_ShouldMatchCount() throws CustomException {
        PayrollService.getInstance().addEmployeeToDB("vijay", "m", 2000000.0, LocalDate.now());
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        Assertions.assertEquals(2, employeeData.size());
    }

    @Test
    void givenMultipleEmployees_WhenAddedToDB_ShouldMatchCount() throws CustomException {
        EmployeeData[]  employeeArray = {
                new EmployeeData(0, "yash", "m", 3000000.0, LocalDate.now()),
                new EmployeeData(0, "jay", "m", 2000000.0, LocalDate.now()),
                new EmployeeData(0, "vijay", "m", 5000000.0, LocalDate.now()),
                new EmployeeData(0, "abhijit", "m", 6000000.0, LocalDate.now()),
                new EmployeeData(0, "bhushan", "m", 8000000.0, LocalDate.now()),
                new EmployeeData(0, "harshal", "m", 1000000.0, LocalDate.now()),
        };
        Instant start = Instant.now();
        PayrollService.getInstance().addMultipleEmployeeToDB(Arrays.asList(employeeArray));
        Instant end = Instant.now();
        System.out.println("Time Taken to Execute Without MultiThreading: " + Duration.between(start, end));
        Instant startThread = Instant.now();
        PayrollService.getInstance().addMultipleEmployeeToDBWithThreads(Arrays.asList(employeeArray));
        Instant endThread = Instant.now();
        System.out.println("Time Taken to Execute With MultiThreading: " + Duration.between(startThread, endThread));
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        Assertions.assertEquals(13, employeeData.size());
    }

    @Test
    void givenMultipleEmployees_WhenUpdatedToDB_ShouldMatchCount() throws CustomException {
        EmployeeData[]  employeeArray = {
                new EmployeeData("yash", 7000000.0),
                new EmployeeData("jay", 8000000.0)
        };
        Instant start = Instant.now();
        PayrollService.getInstance().updateMultipleEmployeeToDB(Arrays.asList(employeeArray));
        Instant end = Instant.now();
        System.out.println("Time Taken to Execute Without MultiThreading: " + Duration.between(start, end));
        System.out.println(PayrollService.getInstance().getEmployeePayrollData("yash"));
        System.out.println(PayrollService.getInstance().getEmployeePayrollData("jay"));
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        Assertions.assertEquals(13, employeeData.size());
    }
}
