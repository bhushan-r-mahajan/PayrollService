import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import  java.sql.Date;
import java.util.Map;

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

    private void preparedStatementForEmployeeData() throws CustomException {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new CustomException("Prepared Statement Failed!");
        }
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
                String gender = result.getString("gender");
                double salary = result.getDouble("salary");
                LocalDate start_date = result.getDate("start_date").toLocalDate();
                employeeData.add(new EmployeeData(id, name, gender, salary, start_date));
            }
        } catch (SQLException e) {
            throw new CustomException("Failed!!");
        }
        return employeeData;
    }

    private List<EmployeeData> getDataFromDBWhenSQLGiven(String sql) throws CustomException {
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            employeeDataList = getDataFromDB(result);
        } catch (SQLException e) {
            throw new CustomException("Failed");
        }
        return employeeDataList;
    }

    private Map<String, Double> getOperationResult(String sql) throws CustomException {
        Map<String, Double> averageByGender = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String gender = result.getString("gender");
                double salary = result.getDouble("operation");
                averageByGender.put(gender, salary);
            }
        } catch (SQLException e) {
            throw new CustomException("Operation Failed!!");
        }
        return averageByGender;
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

    public List<EmployeeData> readAllDataFromTable() throws CustomException {
        String sql = "select * from employee_payroll;";
        return this.getDataFromDBWhenSQLGiven(sql);
    }

    public List<EmployeeData> readDataAccordingToDate(LocalDate startDate, LocalDate endDate) throws CustomException {
        String sql = String.format("select * from employee_payroll where start_date between '%s' and '%s'",
                                    Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getDataFromDBWhenSQLGiven(sql);
    }

    public Map<String, Double> readAverageSalary() throws CustomException {
        String sql = "select gender, avg(salary) as operation from employee_payroll group by gender;";
        return this.getOperationResult(sql);
    }

    public Map<String, Double> readSumOfSalary() throws CustomException {
        String sql = "select gender, sum(salary) as operation from employee_payroll group by gender;";
        return this.getOperationResult(sql);
    }

    public Map<String, Double> readMinSalary() throws CustomException {
        String sql = "select gender, min(salary) as operation from employee_payroll group by gender;";
        return this.getOperationResult(sql);
    }

    public Map<String, Double> readMaxSalary() throws CustomException {
        String sql = "select gender, max(salary) as operation from employee_payroll group by gender;";
        return this.getOperationResult(sql);
    }

    public Map<String, Double> readCountOfSalary() throws CustomException {
        String sql = "select gender, count(salary) as operation from employee_payroll group by gender;";
        return this.getOperationResult(sql);
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

    public void addMultipleEmployeeToDB(List<EmployeeData> employeeDataList) {
        employeeDataList.forEach(employeeData -> {
            System.out.println("Employee Being Added is: " + employeeData.name);
            try {
                this.addEmployeeToDB(employeeData.name, employeeData.gender, employeeData.salary, employeeData.date);
            } catch (CustomException e) {
                try {
                    throw new CustomException("Query Failed!!");
                } catch (CustomException customException) {
                    customException.printStackTrace();
                }
            }
            System.out.println("Employee Added: " + employeeData.name);
        });
        System.out.println(this.employeeDataList);
    }

    public EmployeeData addEmployeeToDB(String name, String gender, double salary, LocalDate date) throws CustomException {
        int id = -1;
        EmployeeData employeeData;
        String sql = String.format("insert into employee_payroll (name, gender, salary, start_date) values " +
                "('%s', '%s', '%s', '%s')", name, gender, salary, date);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) id = resultSet.getInt(1);
            }
            employeeData = new EmployeeData(id, name, gender, salary, date);
        } catch (SQLException e) {
            throw new CustomException("Query Failed!!");
        }
        return employeeData;
    }

    public void addMultipleEmployeeToDBWithThreads(List<EmployeeData> employeeDataList) {
        Map<Integer, Boolean> employeeMultiThread = new HashMap<>();
        employeeDataList.forEach(employeeData -> {
            Runnable task = () -> {
                employeeMultiThread.put(employeeData.hashCode(), false);
                System.out.println("Employee Being Added is: " + Thread.currentThread().getName());
                try {
                    this.addEmployeeToDB(employeeData.name, employeeData.gender, employeeData.salary, employeeData.date);
                } catch (CustomException e) {
                    try {
                        throw new CustomException("Failed!!");
                    } catch (CustomException customException) {
                        customException.printStackTrace();
                    }
                }
                employeeMultiThread.put(employeeData.hashCode(), true);
                System.out.println("Employee Added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeeData.name);
            thread.start();
        });
        while (employeeMultiThread.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.employeeDataList);
    }
}