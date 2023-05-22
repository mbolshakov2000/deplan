import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

@Builder
@Data
@AllArgsConstructor
public class TeachersPanel extends JPanel {
    JLabel teachersLabel;
    JList teachersList;
    JLabel del1, del2, del3, del4;
    JScrollPane scroll1;
    JButton teacherAdd, teacherChange, teacherDelete;
        TeachersPanel() {
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(1200,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(1200, 20));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(350,3));
        del4 = new JLabel();
        del4.setPreferredSize(new Dimension(350, 3));
        Font font = Main.font;
        teachersLabel = new JLabel("Список преподавателей");
        teachersLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        teachersList = new JList();
        teachersList.setFont(font);

        teacherAdd = new JButton("Добавить");
        teacherAdd.setFont(font);
//        teacherAdd.setPreferredSize(new Dimension(300, 30));
        teacherChange = new JButton("Изменить");
        teacherChange.setFont(font);
        teacherDelete = new JButton("Удалить");
        teacherDelete.setFont(font);
        teacherDelete.setPreferredSize(teacherChange.getPreferredSize());
        add(del1);
        add(teachersLabel);
        add(del2);
        add(teachersList);
        scroll1 = new JScrollPane(teachersList);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setPreferredSize(new Dimension(700,300));
        add(scroll1);
        JPanel panel= new JPanel();
        panel.setPreferredSize(new Dimension(200,300));
        panel.add(teacherAdd);
        panel.add(del3);
        panel.add(teacherChange);
        panel.add(del4);
        panel.add(teacherDelete);
        add(panel);
//        add(teacherAdd);
//        add(teacherChange);
//        add(teacherDelete);
    }
}