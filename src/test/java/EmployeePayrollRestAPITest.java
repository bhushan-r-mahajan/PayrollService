import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public class EmployeePayrollRestAPITest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:3000";
        RestAssured.port = 3000;
    }

    private Data[] getEmployeeDetails() {
        Response response = RestAssured.get(RestAssured.baseURI + "/employees");
        System.out.println("Employees in JSON Server are: \n" + response.asString());
        return new Gson().fromJson(response.asString(), Data[].class);
    }

    private Response addEmployeeToJSONServer(Data data) {
        String employeeJSON = new Gson().toJson(data);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(employeeJSON);
        return requestSpecification.post(RestAssured.baseURI + "/employees");
    }


    @Test
    void givenEmployeeDataInJSONServer_WhenFetched_ShouldMatchCount() {
        Data[] data = getEmployeeDetails();
        EmployeePayrollRestAPI employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(data));
        long entries = employeePayrollRestAPI.countEntries();
        Assertions.assertEquals(1, entries);
    }

    @Test
    void givenANewEmployee_WhenAdded_ShouldMatchCount() {
        EmployeePayrollRestAPI employeePayrollRestAPI;
        Data[] dataArray = getEmployeeDetails();
        employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(dataArray));

        Data data;
        data = new Data(0, "vijay", "m", 4000000.0, Date.from(Instant.now()));
        Response response = addEmployeeToJSONServer(data);

        data = new Gson().fromJson(response.asString(), Data.class);
        employeePayrollRestAPI.addEmployeeToList(data);
        System.out.println("<<<<<<<<<- After Adding Into JSON Server ->>>>>>>>>");
        getEmployeeDetails();
        long entries = employeePayrollRestAPI.countEntries();
        Assertions.assertEquals(2, entries);
    }
}
