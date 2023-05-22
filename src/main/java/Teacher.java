import java.util.*;

public class Teacher {
    String id;
    String first_name;
    String last_name;
    String patronymic;
    String position;
    String phone;

    Teacher(String id, String last_name, String first_name, String patronymic, String position, String phone) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.patronymic = patronymic;
        this.position = position;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return
                last_name + " " +
                first_name + " " +
                        patronymic + " - " +
                        position + " - Телефон: " +
                        phone;
    }
}
