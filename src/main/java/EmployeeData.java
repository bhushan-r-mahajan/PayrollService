import java.time.LocalDate;
import java.util.Objects;

public class EmployeeData {
    public int id;
    public String name;
    public String gender;
    public double salary;
    public LocalDate date;

    public EmployeeData(Integer id, String name, String gender, Double salary, LocalDate date) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.date = date;
    }

    public EmployeeData(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }


    @Override
    public String toString() {
        return "EmployeeData:->" + "Id = " + id + " Name = " + name + " Gender = " + gender +" Salary = " + salary + " Start Date = " + date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeData that = (EmployeeData) o;
        return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, salary, date);
    }
}
