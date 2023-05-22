public class CurrentLevel {
    String competence_id;
    String level_id;
    String event_id;
    String result_id;

    CurrentLevel(String competence_id, String level_id, String event_id, String result_id) {
        this.competence_id = competence_id;
        this.level_id = level_id;
        this.event_id = event_id;
        this.result_id = result_id;
    }

    String getCompetence_id(){
        return this.competence_id;
    }

    String getLevel_id(){
        return this.level_id;
    }

    String getEvent_id(){
        return this.event_id;
    }

    String getResult_id(){
        return this.result_id;
    }
}
