public class CompetenceTeacher {
    String employee_id;
    String id;
    String abbreviation;
    String name;
    String level_id;
    String level;
    String score;
    String date;

    CompetenceTeacher(String employee_id, String id, String abbreviation, String name, String level_id, String level, String score, String date) {
        this.employee_id = employee_id;
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.level_id = level_id;
        this.level = level;
        this.score = score;
        this.date = date;
    }

    @Override
    public String toString() {
        return
                abbreviation + " " +
                        name + " " +
                        level + " - " +
                        score + " - " +
                        date;
    }

    public String[] toArray1(){
        return new String[]{abbreviation, name, level, score, date};
    }
}
