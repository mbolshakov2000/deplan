public class Event {
    String id;
    String name;
    String type;
    String description;
    String date_start;
    String date_end;
    String quantity;
    String cost;

    Event(String id, String name, String type, String description, String date_start, String date_end, String quantity, String cost) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.date_start = date_start;
        this.date_end = date_end;
        this.quantity = quantity;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return
                name + " - " +
                type + ". " +
                        date_start + " - " +
                        date_end + ". Кол-во: " + quantity + ". Стоимость: " + cost;
    }
}
