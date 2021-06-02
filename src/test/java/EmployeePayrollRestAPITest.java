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

    @Test
    void givenMultipleEmployees_WhenAdded_ShouldMatchCount() {
        EmployeePayrollRestAPI employeePayrollRestAPI;
        Data[] dataArray = getEmployeeDetails();
        employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(dataArray));

        Data[] arrayOfData = {
                new Data(0, "vijay", "m", 4000000.0, Date.from(Instant.now())),
                new Data(0, "shashank", "m", 6000000.0, Date.from(Instant.now())),
                new Data(0, "abhijit", "m", 8000000.0, Date.from(Instant.now())),
        };
        for (Data data : arrayOfData) {
            Response response = addEmployeeToJSONServer(data);
            data = new Gson().fromJson(response.asString(), Data.class);
            employeePayrollRestAPI.addEmployeeToList(data);
        }

        System.out.println("<<<<<<<<<- After Adding Into JSON Server ->>>>>>>>>");
        getEmployeeDetails();
        long entries = employeePayrollRestAPI.countEntries();
        Assertions.assertEquals(5, entries);
    }

    @Test
    void givenUpdateQuery_WhenUpdated_ShouldReturn200ResponseCode() {
        EmployeePayrollRestAPI employeePayrollRestAPI;
        Data[] dataArray = getEmployeeDetails();
        employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(dataArray));

        employeePayrollRestAPI.updateEmployeeSalary("bhushan", 5000000);
        Data data = employeePayrollRestAPI.getEmployee("bhushan");
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        String employeeJSON = new Gson().toJson(data);
        requestSpecification.body(employeeJSON);
        Response response = requestSpecification.put(RestAssured.baseURI + "/employees/" + data.id);
        System.out.println("After Updating we have: \n" + response.asString());
        int statusCode = response.statusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    void givenDeleteQuery_WhenDeleted_ShouldReturn200ResponseCode() {
        EmployeePayrollRestAPI employeePayrollRestAPI;
        Data[] dataArray = getEmployeeDetails();
        employeePayrollRestAPI = new EmployeePayrollRestAPI(Arrays.asList(dataArray));

        Data data = employeePayrollRestAPI.getEmployee("abhijit");
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        Response response = requestSpecification.delete(RestAssured.baseURI + "/employees/" + data.id);
        System.out.println("After Updating we have: \n" + getEmployeeDetails());
        int statusCode = response.statusCode();
        Assertions.assertEquals(200, statusCode);
    }
}
