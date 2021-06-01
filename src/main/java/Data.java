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
}
