import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PayrollServiceTest {
    List <EmployeeData> employeeData = new ArrayList<>();

    @Test
    void giveDataFromADatabase_WhenRetrived_ShouldMatchTheCount() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        System.out.println("The Number of Entries in DataBase are = " + employeeData.size());
        Assertions.assertEquals(3, employeeData.size());
    }

    @Test
    void givenNewSalary_WhenUpdated_ShouldPassTest() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        int result = PayrollService.getInstance().updateDataInDB("shashank", 3000000);
        System.out.println(employeeData.get(0));
        System.out.println(PayrollService.getInstance().getEmployeePayrollData("Shashank"));
        Assertions.assertEquals(1, result);
    }

    @Test
    void givenNewSalary_WhenUpdated_ShouldSyncWithDB() throws CustomException {
        employeeData = PayrollService.getInstance().readAllDataFromTable();
        PayrollService.getInstance().updateDataInDB("shashank", 3000000);
        boolean result = employeeData.get(0).equals(PayrollService.getInstance().getEmployeePayrollData("Shashank"));
        Assertions.assertTrue(result);
    }
}
