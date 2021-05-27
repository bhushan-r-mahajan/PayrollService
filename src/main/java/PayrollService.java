import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {

    private Connection getConnection() throws CustomException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Bhushan@0611";
        Connection connection;
        try {
            System.out.println("Connecting to database " + jdbcUrl);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connected to " + connection + " Successful!!!!");
        } catch (SQLException e) {
            throw new CustomException("Connection to Database Failed!!");
        }
        return connection;
    }

    public List<EmployeeData> readDataFromDB() throws CustomException {
        String sql = "select * from employee_payroll;";
        List<EmployeeData> employeeData = new ArrayList<>();

        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate start_date = result.getDate("start_date").toLocalDate();
                employeeData.add(new EmployeeData(id, name, salary, start_date));
            }
        } catch (SQLException e) {
            throw new CustomException("Failed");
        }
        return employeeData;
    }
}
