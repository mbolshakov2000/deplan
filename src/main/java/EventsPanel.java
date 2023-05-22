import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Builder
@Data
@AllArgsConstructor
public class EventsPanel extends JPanel {
    JLabel eventsLabel;
    JList eventsList;
    JLabel del1, del2, del3, del4;
    JScrollPane scroll1;
    JButton eventAdd, eventChange, eventDelete;
        EventsPanel() {
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(1200,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(1200, 20));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(350,3));
        del4 = new JLabel();
        del4.setPreferredSize(new Dimension(350, 3));
        Font font = Main.font;
        eventsLabel = new JLabel("Список мероприятий");
        eventsLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        eventsList = new JList();
        eventsList.setFont(font);

        eventAdd = new JButton("Добавить");
        eventAdd.setFont(font);
        eventChange = new JButton("Изменить");
        eventChange.setFont(font);
        eventDelete = new JButton("Удалить");
        eventDelete.setFont(font);
        eventDelete.setPreferredSize(eventChange.getPreferredSize());
        add(del1);
        add(eventsLabel);
        add(del2);
        add(eventsList);
        scroll1 = new JScrollPane(eventsList);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setPreferredSize(new Dimension(700,300));
        add(scroll1);
        JPanel panel= new JPanel();
        panel.setPreferredSize(new Dimension(200,300));
        panel.add(eventAdd);
        panel.add(del3);
        panel.add(eventChange);
        panel.add(del4);
        panel.add(eventDelete);
        add(panel);
    }
}