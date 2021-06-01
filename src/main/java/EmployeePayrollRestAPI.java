import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestAPI {

    List<Data> DataList;

    public EmployeePayrollRestAPI(List<Data> DataList) {
        this.DataList = new ArrayList<>(DataList);
    }

    public long countEntries() {
        return this.DataList.size();
    }
}
