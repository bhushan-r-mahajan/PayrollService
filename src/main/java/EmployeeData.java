import java.time.LocalDate;

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

    @Override
    public String toString() {
        return "EmployeeData:->" + "Id = " + id + " Name = " + name + " Salary = " + salary + " Start Date = " + date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeData that = (EmployeeData) o;
        return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name) && date.equals(that.date);
    }
}
