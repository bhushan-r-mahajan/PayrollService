import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
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
    void givenPayrollData_WhenQAverageSalaryRetrieved_ShouldGiveResultGenderWise() throws CustomException {
        Map<String, Double> averageSalary = PayrollService.getInstance().readAverageSalary();
        System.out.println(PayrollService.getInstance().readAverageSalary());
        Assertions.assertTrue(averageSalary.get("m").equals(3500000.0) && averageSalary.get("f").equals(3000000.0));
    }
}
