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
}
