import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EmployeePayrollRestAPITest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:3000";
        RestAssured.port = 3000;
    }

    @Test
    void givenEmployeeDataInJSONServer_WhenFetched_ShouldMatchCount() {
        Response response = RestAssured.get(RestAssured.baseURI + "/employees");
        System.out.println("Employees in JSON Server are: \n" + response.asString());
        Data[] data = new Gson().fromJson(response.asString(), Data[].class);
        EmployeePayrollRestAPI employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(data));
        long entries = employeePayrollRestAPI.countEntries();
        Assertions.assertEquals(1, entries);
    }
}
