import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Builder
@Data
@AllArgsConstructor
public class CompetencesPanel extends JPanel {
    JLabel competencesLabel;
    JList competencesList;
    JLabel del1, del2;
    JScrollPane scroll1;
    JButton competenceCheck;
        CompetencesPanel() {
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(1200,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(1200, 20));
        Font font = Main.font;
        competencesLabel = new JLabel("Список компетенций");
        competencesLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        competencesList = new JList();
        competencesList.setFont(font);

        competenceCheck = new JButton("Прочитать");
        competenceCheck.setFont(font);
        add(del1);
        add(competencesLabel);
        add(del2);
        add(competencesList);
        scroll1 = new JScrollPane(competencesList);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setPreferredSize(new Dimension(700,300));
        add(scroll1);
        JPanel panel= new JPanel();
        panel.setPreferredSize(new Dimension(200,300));
        panel.add(competenceCheck);
        add(panel);
    }
}