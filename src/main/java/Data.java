import java.util.Date;

public class Data {
    public int id;
    public String name;
    public String gender;
    public double salary;
    public Date date;

    public Data(Integer id, String name, String gender, Double salary, Date date) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.date = date;
    }

    @Override
    public String toString() {
        return "EmployeeData:->" + "Id = " + id + " Name = " + name + " Gender = " + gender +" Salary = " + salary + " Start Date = " + date;
    }
}
