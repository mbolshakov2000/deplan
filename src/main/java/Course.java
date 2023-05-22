import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    String date;
    String name;
    ArrayList<String> comp;

    String get(int i) {
        return comp.get(i);
    }

    int getCount() {
        return comp.size();
    }

}
