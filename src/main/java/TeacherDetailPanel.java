import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

@Builder
@Data
@AllArgsConstructor
public class TeacherDetailPanel extends JPanel {
    JLabel fio1Label, fio2Label, firstNameLabel, lastNameLabel, patronymicLabel, positionLabel, phoneLabel, del, del1, del2, del3, delc, delb, compLabel;
    JTextField firstNameField, lastNameField, patronymicField, phoneField;
    JButton cancel, submit;
    JTable competenceTable;
    JComboBox positionField;
    JButton competenceAdd, competenceChange, competenceDelete;
    TeacherDetailPanel(Teacher t, ArrayList<CompetenceTeacher> competenceTeachers) {
        Font font = Main.font;
        del = new JLabel();
        del.setPreferredSize(new Dimension(900,30));
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(900,30));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(900,10));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(900,10));
        delc = new JLabel();
        delc.setPreferredSize(new Dimension(700,30));
        delb = new JLabel();
        delb.setPreferredSize(new Dimension(200,40));
        fio1Label = new JLabel("ФИО: ");
        fio1Label.setFont(font);
        fio1Label.setPreferredSize(new Dimension(200, 30));
        lastNameLabel = new JLabel("Фамилия: ");
        lastNameLabel.setFont(font);
        lastNameLabel.setPreferredSize(new Dimension(200, 30));
        firstNameLabel = new JLabel("Имя: ");
        firstNameLabel.setFont(font);
        firstNameLabel.setPreferredSize(new Dimension(200, 30));
        patronymicLabel = new JLabel("Отчество: ");
        patronymicLabel.setFont(font);
        patronymicLabel.setPreferredSize(new Dimension(200, 30));
        fio2Label = new JLabel();
        fio2Label.setFont(font);
        fio2Label.setPreferredSize(new Dimension(700,30));
        firstNameField = new JTextField();
        firstNameField.setFont(font);
        firstNameField.setPreferredSize(new Dimension(700,30));
        lastNameField = new JTextField();
        lastNameField.setFont(font);
        lastNameField.setPreferredSize(new Dimension(700,30));
        patronymicField = new JTextField();
        patronymicField.setFont(font);
        patronymicField.setPreferredSize(new Dimension(700,30));
        positionLabel = new JLabel("Должность: ");
        positionLabel.setFont(font);
        positionLabel.setPreferredSize(new Dimension(200, 30));
        positionField = new JComboBox(new String[]{"Не определен", "Аспирант", "Ассистент", "Ведущий научный сотрудник",
                "Главный научный сотрудник", "Докторант", "Доцент", "Младший научный сотрудник",
                "Научный сотрудник", "Преподаватель", "Профессор", "Старший преподаватель", "Стажер",
                "Старший научный сотрудник", "Студент"});
        positionField.setFont(font);
        positionField.setPreferredSize(new Dimension(700,30));
        phoneLabel = new JLabel("Телефон: ");
        phoneLabel.setFont(font);
        phoneLabel.setPreferredSize(new Dimension(200, 30));
        phoneField = new JTextField();
        phoneField.setFont(font);
        phoneField.setPreferredSize(new Dimension(700,30));
        cancel = new JButton("Назад");
        submit = new JButton("Сохранить");
        submit.setFont(font);
        cancel.setFont(font);

        try {
            fio2Label.setText(t.last_name+" "+t.first_name+" "+t.patronymic);
            positionField.setSelectedItem(t.position);
            phoneField.setText(t.phone);
            add(fio1Label);
            add(fio2Label);
        }
        catch (NullPointerException e){
            lastNameField.setText("");
            firstNameField.setText("");
            patronymicField.setText("");
            positionField.setSelectedItem(0);
            phoneField.setText("");
            add(lastNameLabel);
            add(lastNameField);
            add(firstNameLabel);
            add(firstNameField);
            add(patronymicLabel);
            add(patronymicField);
        }

        add(positionLabel);
        add(positionField);
        add(phoneLabel);
        add(phoneField);
        add(del1);

        competenceAdd = new JButton("Добавить");
        competenceChange = new JButton("Изменить");
        competenceDelete = new JButton("Удалить");
        competenceAdd.setFont(font);
        competenceChange.setFont(font);
        competenceDelete.setFont(font);
        competenceDelete.setPreferredSize(competenceAdd.getPreferredSize());
        if (t != null) {
            compLabel = new JLabel("Компетенции сотрудника:");
            compLabel.setFont(font);
            add(compLabel);
            add(delc);

            String[][] arrayCompetences = new String[competenceTeachers.size()][5];
            for (int i = 0; i < competenceTeachers.size(); i++) {
                arrayCompetences[i] = competenceTeachers.get(i).toArray1();
            }
            String[] columnsHeader = new String[]{"Код", "Название",
                    "Уровень", "Балл", "Дата"};

            competenceTable = new JTable(arrayCompetences, columnsHeader);
            competenceTable.setFont(font);
            JTableHeader header = competenceTable.getTableHeader();
            header.setFont(font);
            TableColumnModel columnModel = competenceTable.getColumnModel();
            Enumeration<TableColumn> e = columnModel.getColumns();
            TableColumn column = (TableColumn) e.nextElement();
            column.setMinWidth(50);
            column.setMaxWidth(50);
            column = (TableColumn) e.nextElement();
            column = (TableColumn) e.nextElement();
            column.setMinWidth(150);
            column.setMaxWidth(160);
            column = (TableColumn) e.nextElement();
            column.setMinWidth(50);
            column.setMaxWidth(50);
            column = (TableColumn) e.nextElement();
            column.setMinWidth(120);
            column.setMaxWidth(140);

            JScrollPane scroll1 = new JScrollPane(competenceTable);
            scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll1.setPreferredSize(new Dimension(700, 300));
            add(scroll1);

            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 300));

            panel.add(delb);
            panel.add(competenceAdd);
//        panel.add(del2);
//        panel.add(competenceChange);
            panel.add(del3);
            panel.add(competenceDelete);
            add(panel);
        }
        add(del);
        add(cancel);
        add(submit);
    }
}