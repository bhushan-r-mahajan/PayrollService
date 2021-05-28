import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {
    List<EmployeeData> employeeDataList = new ArrayList<>();

    private PreparedStatement employeePayrollDataStatement;
    private static PayrollService instance;

    public static PayrollService getInstance() {
        if (instance == null) {
            instance = new PayrollService();
        }
        return instance;
    }

    private Connection getConnection() throws CustomException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Bhushan@0611";
        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (SQLException e) {
            throw new CustomException("Connection to Database Failed!!");
        }
        return connection;
    }

    private List<EmployeeData> getDataFromDB(ResultSet result) throws CustomException {
        List<EmployeeData> employeeData = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate start_date = result.getDate("start_date").toLocalDate();
                employeeData.add(new EmployeeData(id, name, salary, start_date));
            }
        } catch (SQLException e) {
            throw new CustomException("Failed!!");
        }
        return employeeData;
    }

    public List<EmployeeData> readAllDataFromTable() throws CustomException {
        String sql = "select * from employee_payroll;";
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            employeeDataList = getDataFromDB(result);
        } catch (SQLException e) {
            throw new CustomException("Failed");
        }
        return employeeDataList;
    }

    public List<EmployeeData> getEmployeePayrollData(String fname) throws CustomException {
        if(this.employeePayrollDataStatement == null){
            this.preparedStatementForEmployeeData();
        }
        try {
            employeePayrollDataStatement.setString(1, fname);
            ResultSet result = employeePayrollDataStatement.executeQuery();
            employeeDataList = getDataFromDB(result);
        }catch (SQLException e) {
            throw new CustomException("Failed!!");
        }
        return employeeDataList;
    }

    private void preparedStatementForEmployeeData() throws CustomException {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new CustomException("Prepared Statement Failed!");
        }
    }

    public int updateDataInDB(String name, double salary) throws CustomException {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new CustomException("Update Failed!!!");
        }
    }
}