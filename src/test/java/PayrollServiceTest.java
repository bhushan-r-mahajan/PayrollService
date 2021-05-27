import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PayrollServiceTest {
    @Test
    void giveDataFromADatabase_WhenRetrived_ShouldMatchTheCount() throws CustomException {
        PayrollService payrollService = new PayrollService();
        List <EmployeeData> employeeData = payrollService.readDataFromDB();
        System.out.println("The Number of Entries in DataBase are = " + employeeData.size());
        Assertions.assertEquals(3, employeeData.size());
    }
}
