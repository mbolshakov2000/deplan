public class CompetenceEvent {
    String event_id;
    String id;
    String abbreviation;
    String name;
    String level_id;
    String level;
    String result_id;
    String result;

    CompetenceEvent(String event_id, String id, String abbreviation, String name, String level_id, String level, String result_id, String result) {
        this.event_id = event_id;
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.level_id = level_id;
        this.level = level;
        this.result_id = result_id;
        this.result = result;
    }

    @Override
    public String toString() {
        return
                abbreviation + " " +
                        name + " " +
                        level + " - " +
                        result;
    }

    public String[] toArray1(){
        return new String[]{abbreviation, name, level, result};
    }
}
