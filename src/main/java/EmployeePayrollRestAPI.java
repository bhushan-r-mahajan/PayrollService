import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestAPI {

    List<Data> dataList;

    public EmployeePayrollRestAPI(List<Data> dataList) {
        this.dataList = new ArrayList<>(dataList);
    }

    public long countEntries() {
        return this.dataList.size();
    }

    public void addEmployeeToList(Data data) {
        this.dataList.add(data);
    }

    public void updateEmployeeSalary(String name, double salary) {
        Data data = this.getEmployee(name);
        if(data != null) {
            data.salary = salary;
        }
    }

    public Data getEmployee(String name) {
        return this.dataList.stream().filter(dataItem -> dataItem.name.equals(name)).findFirst().orElse(null);
    }
}
