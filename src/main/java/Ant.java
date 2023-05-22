import java.util.*;

public class Ant {
    ArrayList<Integer> way;
    double distance;

    Ant() {
        way = new ArrayList<Integer>();
        distance = 0;
    }

    void add(int s) {
        way.add(s);
    }

    int get(int i) {
        return way.get(i);
    }

    void set(int i, int j){way.set(i,j);}

    int getCount() {
        return way.size();
    }
}