public class Competence {
    String id;
    String abbreviation;
    String name;
    String description;

    Competence(String id, String abbreviation, String name, String description) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return
                abbreviation + " - " +
                        name;
    }

    public String[] toArray1(){
        return new String[]{abbreviation, name, description};
    }
}
