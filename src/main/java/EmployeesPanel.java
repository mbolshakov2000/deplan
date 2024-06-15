import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Builder
@Data
@AllArgsConstructor
public class EmployeesPanel extends JPanel {
    JLabel employeesLabel;
    JList employeesList;
    JLabel del1, del2, del3, del4;
    JScrollPane scroll1;
    JButton employeeAdd, employeeChange, employeeDelete;
        EmployeesPanel() {
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(1200,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(1200, 20));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(350,3));
        del4 = new JLabel();
        del4.setPreferredSize(new Dimension(350, 3));
        Font font = Main.font;
        employeesLabel = new JLabel("Список сотрудников");
        employeesLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        employeesList = new JList();
        employeesList.setFont(font);

        employeeAdd = new JButton("Добавить");
        employeeAdd.setFont(font);
//        employeeAdd.setPreferredSize(new Dimension(300, 30));
        employeeChange = new JButton("Изменить");
        employeeChange.setFont(font);
        employeeDelete = new JButton("Удалить");
        employeeDelete.setFont(font);
        employeeDelete.setPreferredSize(employeeChange.getPreferredSize());
        add(del1);
        add(employeesLabel);
        add(del2);
        add(employeesList);
        scroll1 = new JScrollPane(employeesList);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setPreferredSize(new Dimension(700,300));
        add(scroll1);
        JPanel panel= new JPanel();
        panel.setPreferredSize(new Dimension(200,300));
        panel.add(employeeAdd);
        panel.add(del3);
        panel.add(employeeChange);
        panel.add(del4);
        panel.add(employeeDelete);
        add(panel);
//        add(employeeAdd);
//        add(employeeChange);
//        add(employeeDelete);
    }
}